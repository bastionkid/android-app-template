package com.azuredragon.core.time

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

object Clock {

    val ONE_SECOND_IN_MILLIS = 1.seconds.inWholeMilliseconds

    private val ONE_MINUTE_IN_MILLIS = 1.minutes.inWholeMilliseconds

    private val ONE_HOUR_IN_MILLIS = 1.hours.inWholeMilliseconds

    private val ONE_DAY_IN_MILLIS = 1.days.inWholeMilliseconds

    val secondTicker: Flow<Long> = getTimeTicker(ONE_SECOND_IN_MILLIS)

    val minuteTicker: Flow<Long> = getTimeTicker(ONE_MINUTE_IN_MILLIS)

    val hourTicker: Flow<Long> = getTimeTicker(ONE_HOUR_IN_MILLIS)

    val dayTicker: Flow<Long> = getTimeTicker(ONE_DAY_IN_MILLIS)

    @OptIn(ExperimentalTime::class)
    private fun getTimeTicker(tickDuration: Long) = flow {
        while (currentCoroutineContext().isActive) {
            delay(tickDuration)
            emit(System.currentTimeMillis())
        }
    }
}