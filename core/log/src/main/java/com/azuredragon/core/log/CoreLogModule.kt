package com.azuredragon.core.log

import org.koin.dsl.module

fun getCoreLogModule(
    defaultTag: String,
    isDebug: Boolean,
) = module {
    single<Logger> {
        LoggerImpl(defaultTag = defaultTag, isDebug = isDebug)
    }

    factory<Logger> { parameters ->
        LoggerImpl(defaultTag = parameters.get(), isDebug = isDebug)
    }
}