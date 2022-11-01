package com.azuredragon.core.time

import com.azuredragon.core.time.ClockImpl.Companion.ONE_DAY_IN_HOURS
import com.azuredragon.core.time.ClockImpl.Companion.ONE_HOUR_IN_MINUTES
import com.azuredragon.core.time.ClockImpl.Companion.ONE_HOUR_IN_SECONDS
import com.azuredragon.core.time.ClockImpl.Companion.ONE_MINUTE_IN_SECONDS

val Number.twoDigitFormattedTime: String
    get() = when (this) {
        is Int,
        is Long -> {
            String.format("%02d", this)
        }
        else -> {
            throw IllegalArgumentException("getTwoDigitFormattedTime does not support ${this::class.simpleName} type")
        }
    }

val Number.formatWithTimeSeparator: String
    get() = when (this) {
        is Int,
        is Long -> {
            val seconds = this.toLong()

            if (seconds > 0) {
                when {
                    seconds < ONE_MINUTE_IN_SECONDS -> "00:${seconds.twoDigitFormattedTime}"
                    seconds < ONE_HOUR_IN_SECONDS -> {
                        val minutes = (seconds / ONE_MINUTE_IN_SECONDS)
                        "${minutes.twoDigitFormattedTime}:${(seconds % ONE_MINUTE_IN_SECONDS).twoDigitFormattedTime}"
                    }
                    else -> {
                        val hours = (seconds / ONE_HOUR_IN_SECONDS)
                        val minutes = (seconds / ONE_MINUTE_IN_SECONDS) % ONE_HOUR_IN_MINUTES

                        if (hours > ONE_DAY_IN_HOURS) {
                            val days = hours / ONE_DAY_IN_HOURS
                            val remainingHours = hours % ONE_DAY_IN_HOURS
                            "${days.twoDigitFormattedTime}:${remainingHours.twoDigitFormattedTime}:${minutes.twoDigitFormattedTime}:${(seconds % ONE_MINUTE_IN_SECONDS).twoDigitFormattedTime}"
                        } else {
                            "${hours.twoDigitFormattedTime}:${minutes.twoDigitFormattedTime}:${(seconds % ONE_MINUTE_IN_SECONDS).twoDigitFormattedTime}"
                        }
                    }
                }
            } else {
                "00:00"
            }
        }
        else -> {
            throw IllegalArgumentException("getTwoDigitFormattedTime does not support ${this::class.simpleName} type")
        }
    }