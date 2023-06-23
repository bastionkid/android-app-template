package com.azuredragon.core.log

import android.util.Log

class LoggerImpl(
    private val defaultTag: String,
    private val isDebug: Boolean,
): Logger {

    override fun v(tag: String?, message: String) {
        if (isDebug) Log.v(tag ?: defaultTag, message)
    }

    override fun i(tag: String?, message: String) {
        if (isDebug) Log.i(tag ?: defaultTag, message)
    }

    override fun d(tag: String?, message: String) {
        if (isDebug) Log.d(tag ?: defaultTag, message)
    }

    override fun w(tag: String?, message: String) {
        if (isDebug) Log.w(tag ?: defaultTag, message)
    }

    override fun w(tag: String?, message: String, throwable: Throwable) {
        if (isDebug) Log.w(tag ?: defaultTag, message, throwable)
    }

    override fun e(tag: String?, message: String, throwable: Throwable?) {
        if (isDebug) Log.e(tag ?: defaultTag, message, throwable)
    }
}