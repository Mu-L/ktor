/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.features

import io.ktor.sessions.*
import kotlin.math.*
import kotlin.time.*

/**
 * Duration to tell the client to keep CORS options.
 */
@ExperimentalTime
public var CORS.Configuration.maxAgeDuration: Duration
    get() = Duration.seconds(maxAgeInSeconds)
    set(newMaxAge) {
        require(!newMaxAge.isNegative()) { "Only non-negative durations can be specified" }
        maxAgeInSeconds = newMaxAge.toDouble(DurationUnit.SECONDS).roundToLong()
    }

@ExperimentalTime
public var HSTS.Configuration.maxAgeDuration: Duration
    get() = Duration.seconds(maxAgeInSeconds)
    set(newMaxAge) {
        require(!newMaxAge.isNegative()) { "Only non-negative durations can be specified" }
        maxAgeInSeconds = newMaxAge.toDouble(DurationUnit.SECONDS).roundToLong()
    }

/**
 * Cookie time to live duration or `null` for session cookies.
 * Session cookies are client-driven. For example, a web browser usually removes session
 * cookies at browser or window close unless the session is restored.
 */
@ExperimentalTime
public var CookieConfiguration.maxAge: Duration?
    get() = Duration.seconds(maxAgeInSeconds)
    set(newMaxAge) {
        require(newMaxAge == null || !newMaxAge.isNegative()) { "Only non-negative durations can be specified" }
        maxAgeInSeconds = newMaxAge?.toDouble(DurationUnit.SECONDS)?.roundToLong() ?: 0L
    }
