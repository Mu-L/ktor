/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.server.testing

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.server.engine.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import kotlin.coroutines.*

/**
 * Represents test call response received from server
 * @property readResponse if response channel need to be consumed into byteContent
 */
public class TestApplicationResponse(
    call: TestApplicationCall,
    private val readResponse: Boolean = false
) : BaseApplicationResponse(call), CoroutineScope by call {

    /**
     * Response body text content. Could be blocking. Remains `null` until response appears.
     */
    val content: String?
        get() {
            val charset = headers[HttpHeaders.ContentType]?.let { ContentType.parse(it).charset() } ?: Charsets.UTF_8
            return byteContent?.toString(charset)
        }

    /**
     * Response body byte content. Could be blocking. Remains `null` until response appears.
     */
    var byteContent: ByteArray? = null
        get() = when {
            field != null -> field
            responseChannel == null -> null
            else -> runBlocking { responseChannel!!.toByteArray() }
        }
        private set

    @Volatile
    private var responseChannel: ByteReadChannel? = null

    @Volatile
    private var responseJob: Job? = null

    /**
     * Get completed when a response channel is assigned.
     * A response could have no channel assigned in some particular failure cases so the deferred could
     * remain incomplete or become completed exceptionally.
     */
    internal val responseChannelDeferred: CompletableJob = Job()

    override fun setStatus(statusCode: HttpStatusCode) {}

    override val headers: ResponseHeaders = object : ResponseHeaders() {
        private val builder = HeadersBuilder()

        override fun engineAppendHeader(name: String, value: String) {
            builder.append(name, value)
        }

        override fun getEngineHeaderNames(): List<String> = builder.names().toList()
        override fun getEngineHeaderValues(name: String): List<String> = builder.getAll(name).orEmpty()
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun responseChannel(): ByteWriteChannel {
        val result = ByteChannel(autoFlush = true)

        if (readResponse) {
            launchResponseJob(result)
        }

        val job = GlobalScope.reader(responseJob ?: EmptyCoroutineContext) {
            channel.copyAndClose(result, Long.MAX_VALUE)
        }

        if (responseJob == null) {
            responseJob = job
        }

        responseChannel = result
        responseChannelDeferred.complete()

        return job.channel
    }

    private fun launchResponseJob(source: ByteReadChannel) {
        responseJob = async(Dispatchers.Default) {
            byteContent = source.toByteArray()
        }
    }

    override suspend fun respondOutgoingContent(content: OutgoingContent) {
        super.respondOutgoingContent(content)
        responseChannelDeferred.completeExceptionally(IllegalStateException("No response channel assigned"))
    }

    /**
     * Response body content channel
     */
    public fun contentChannel(): ByteReadChannel? = byteContent?.let { ByteReadChannel(it) }

    internal suspend fun awaitForResponseCompletion() {
        responseJob?.join()
    }

    // Websockets & upgrade
    private val webSocketCompleted: CompletableJob = Job()

    override suspend fun respondUpgrade(upgrade: OutgoingContent.ProtocolUpgrade) {
        upgrade.upgrade(
            call.receiveChannel(),
            responseChannel(),
            Dispatchers.Default,
            Dispatchers.Default
        ).invokeOnCompletion {
            webSocketCompleted.complete()
        }
    }

    /**
     * Wait for websocket session completion
     */
    public fun awaitWebSocket(durationMillis: Long): Unit = runBlocking {
        withTimeout(durationMillis) {
            responseChannelDeferred.join()
            responseJob?.join()
            webSocketCompleted.join()
        }

        Unit
    }

    /**
     * Websocket session's channel
     */
    public fun websocketChannel(): ByteReadChannel? = responseChannel
}
