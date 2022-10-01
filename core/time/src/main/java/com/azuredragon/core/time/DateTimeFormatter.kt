package com.azuredragon.core.time

object DateTimeFormatter {

    fun formatWithTimeSeparator(seconds: Long): String {
        return if (seconds > 0) {
            when {
                seconds < 60 -> "00:${getTwoDigitFormattedTime(seconds)}"
                seconds < 3600 -> {
                    val minutes = (seconds / 60)
                    "${getTwoDigitFormattedTime(minutes)}:${getTwoDigitFormattedTime(seconds % 60)}"
                }
                else -> {
                    val hours = (seconds / 3600)
                    val minutes = (seconds / 60) % 60
                    if (hours > 24) {
                        val days = hours / 24
                        val remainingHours = hours % 24
                        return "${getTwoDigitFormattedTime(days)}:${getTwoDigitFormattedTime(remainingHours)}:${getTwoDigitFormattedTime(minutes)}:${getTwoDigitFormattedTime(seconds % 60)}"
                    }

                    "${getTwoDigitFormattedTime(hours)}:${getTwoDigitFormattedTime(minutes)}:${getTwoDigitFormattedTime(seconds % 60)}"
                }
            }
        } else {
            "00:00"
        }
    }

    private infix fun getTwoDigitFormattedTime(time: Long) = String.format("%02d", time)
}