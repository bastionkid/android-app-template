package com.azuredragon.core.time

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDateTime

fun Instant.totoJavaLocalDateTime(timeZone: TimeZone): LocalDateTime {
	return this.toLocalDateTime(timeZone).toJavaLocalDateTime()
}
