package com.azuredragon.core.time

import android.os.Parcelable
import com.azuredragon.core.time.ClockImpl.Companion.ONE_DAY_IN_HOURS
import com.azuredragon.core.time.ClockImpl.Companion.ONE_DAY_IN_MILLIS
import com.azuredragon.core.time.ClockImpl.Companion.ONE_HOUR_IN_MILLIS
import com.azuredragon.core.time.ClockImpl.Companion.ONE_HOUR_IN_MINUTES
import com.azuredragon.core.time.ClockImpl.Companion.ONE_MINUTE_IN_MILLIS
import com.azuredragon.core.time.ClockImpl.Companion.ONE_MINUTE_IN_SECONDS
import com.azuredragon.core.time.ClockImpl.Companion.ONE_SECOND_IN_MILLIS
import kotlinx.parcelize.Parcelize

@Parcelize
class TimeModel(
	val days: Int,
	val hours: Int,
	val minutes: Int,
	val seconds: Int,
) : Parcelable {
	companion object {
		fun createFromTimeInMillis(timeInMillis: Long): TimeModel {
			val seconds = (timeInMillis / ONE_SECOND_IN_MILLIS) % ONE_MINUTE_IN_SECONDS
			val minutes = (timeInMillis / ONE_MINUTE_IN_MILLIS) % ONE_HOUR_IN_MINUTES
			val hours = (timeInMillis / ONE_HOUR_IN_MILLIS) % ONE_DAY_IN_HOURS
			val days = (timeInMillis / ONE_DAY_IN_MILLIS)

			return TimeModel(
				days = days.toInt(),
				hours = hours.toInt(),
				minutes = minutes.toInt(),
				seconds = seconds.toInt(),
			)
		}
	}
}
