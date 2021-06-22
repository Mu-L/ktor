/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.features

import kotlin.math.*
import kotlin.time.*

/**
 * Duration to tell the client to keep CORS options.
 */
@ExperimentalTime
public var CORS.Configuration.maxAgeDuration: Duration
    get() = maxAgeInSeconds.seconds
    set(newMaxAge) {
        require(!newMaxAge.isNegative()) { "Only non-negative durations can be specified" }
        maxAgeInSeconds = newMaxAge.inSeconds.roundToLong()
    }
