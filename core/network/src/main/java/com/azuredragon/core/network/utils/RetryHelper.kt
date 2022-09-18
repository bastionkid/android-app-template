package com.azuredragon.core.network.utils

import java.util.*
import kotlin.math.pow

class RetryHelper(
    private val maxDelayMillis: Long = 15_000,
    private val maxRetryCount: Int = 5,
) {
    private val rand = Random()

    private var enabled = true
    var attempts = 0
        private set

    fun reset() {
        enabled = true
        attempts = 0
    }

    fun disable() {
        enabled = false
    }

    fun canRetry(): Boolean {
        if (attempts >= maxRetryCount) {
            return false
        }

        return enabled
    }

    fun nextDelay(): Long {
        if (!canRetry()) return maxDelayMillis

        val delay = (2.0.pow(attempts) - 1) * 1000
        val maxJitter = delay * 0.2
        var jitter = rand.nextDouble() * maxJitter
        //randomly change jitter's sign
        if (rand.nextBoolean()) {
            jitter *= -1
        }

        val finalDelay = (delay + jitter).coerceAtMost(maxDelayMillis.toDouble()).toLong()

        attempts++
        return finalDelay
    }
}