package com.azuredragon.core.time

interface DateTimeFormatter {

	fun parseToDateInMillis(dateString: String): Long

	fun getContestDateRange(startDate: Long, endDate: Long): String

	fun getContestDateRange(startDate: String, endDate: String): String
}
