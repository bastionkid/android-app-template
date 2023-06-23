package com.azuredragon.core.time

import com.azuredragon.core.log.Logger
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class DateTimeFormatterImpl(
	val logger: Logger,
) : DateTimeFormatter {

	private val dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd MMM")

	override fun parseToDateInMillis(dateString: String): Long {
		return dateString.toInstant().toEpochMilliseconds()
	}

	override fun getDateRange(startDate: Long, endDate: Long): String {
		val timeZone = TimeZone.currentSystemDefault()

		val startDateTime = Instant.fromEpochMilliseconds(startDate).totoJavaLocalDateTime(timeZone)
		val endDateTime = Instant.fromEpochMilliseconds(endDate).totoJavaLocalDateTime(timeZone)

		return "${dateFormatter.format(startDateTime)} to ${dateFormatter.format(endDateTime)}"
	}

	override fun getDateRange(startDate: String, endDate: String): String {
		return getDateRange(
			startDate = startDate.toInstant().toEpochMilliseconds(),
			endDate = endDate.toInstant().toEpochMilliseconds(),
		)
	}
}
