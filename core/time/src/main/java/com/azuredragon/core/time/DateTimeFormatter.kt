package com.azuredragon.core.time

interface DateTimeFormatter {

	fun parseToDateInMillis(dateString: String): Long

	fun getDateRange(startDate: Long, endDate: Long): String

	fun getDateRange(startDate: String, endDate: String): String
}
