package com.azuredragon.core.time

import com.azuredragon.core.log.Logger
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlinx.datetime.Clock as KClock

class ClockImpl(
    val logger: Logger,
): Clock {

    override val secondTicker: Flow<Long> = getTimeTicker(1.seconds)

    override val minuteTicker: Flow<Long> = getTimeTicker(1.minutes)

    override val hourTicker: Flow<Long> = getTimeTicker(1.hours)

    override val dayTicker: Flow<Long> = getTimeTicker(1.days)

    override fun getTimeTicker(tickDuration: Duration): Flow<Long> = flow {
        while (currentCoroutineContext().isActive) {
            delay(tickDuration)
            emit(KClock.System.now().toEpochMilliseconds())
        }
    }

    override fun getCountDownTimer(endTime: Long, tickDuration: Duration): Flow<TimeModel> = flow {
        while (currentCoroutineContext().isActive) {
            delay(tickDuration)

            val remainingTime = endTime - KClock.System.now().toEpochMilliseconds()
            emit(TimeModel.createFromTimeInMillis(remainingTime))
        }
    }

    companion object {
        internal val ONE_SECOND_IN_MILLIS = 1.seconds.inWholeMilliseconds

        internal val ONE_MINUTE_IN_MILLIS = 1.minutes.inWholeMilliseconds
        internal val ONE_MINUTE_IN_SECONDS = 1.minutes.inWholeSeconds

        internal val ONE_HOUR_IN_MILLIS = 1.hours.inWholeMilliseconds
        internal val ONE_HOUR_IN_MINUTES = 1.hours.inWholeMinutes
        internal val ONE_HOUR_IN_SECONDS = 1.hours.inWholeSeconds

        internal val ONE_DAY_IN_MILLIS = 1.days.inWholeMilliseconds
        internal val ONE_DAY_IN_HOURS = 1.days.inWholeHours
    }
}