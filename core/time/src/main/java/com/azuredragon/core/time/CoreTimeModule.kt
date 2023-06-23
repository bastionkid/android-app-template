package com.azuredragon.core.time

import org.koin.dsl.module

fun getCoreTimeModule(isDebug: Boolean) = module {
    single<Clock> { ClockImpl(get()) }
    single<DateTimeFormatter> { DateTimeFormatterImpl(get()) }
}