package com.azuredragon.core.time

import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

interface Clock {

    val secondTicker: Flow<Long>
    val minuteTicker: Flow<Long>
    val hourTicker: Flow<Long>
    val dayTicker: Flow<Long>

    fun getTimeTicker(tickDuration: Duration): Flow<Long>

    fun getCountDownTimer(endTime: Long, tickDuration: Duration): Flow<TimeModel>
}