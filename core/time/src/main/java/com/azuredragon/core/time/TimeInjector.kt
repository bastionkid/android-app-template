package com.azuredragon.core.time

import android.annotation.SuppressLint
import android.content.Context
import com.azuredragon.core.log.Logger

@SuppressLint("StaticFieldLeak")
object TimeInjector {

	private lateinit var applicationContext: Context
	private lateinit var logger: Logger
	var isDebug: Boolean = false
		private set

	private lateinit var clock: Clock
	private lateinit var dateTimeFormatter: DateTimeFormatter

	fun inject(
		applicationContext: Context,
		logger: Logger,
		isDebug: Boolean,
	) {
		this.applicationContext = applicationContext
		this.logger = logger
		this.isDebug = isDebug

		this.clock = ClockImpl(logger)
		this.dateTimeFormatter = DateTimeFormatterImpl(logger)
	}

	fun provideClock(): Clock {
		return clock
	}

	fun provideDateTimeFormatter(): DateTimeFormatter {
		return dateTimeFormatter
	}
}
