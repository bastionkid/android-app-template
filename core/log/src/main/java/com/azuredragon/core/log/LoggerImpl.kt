package com.azuredragon.core.log

import android.util.Log

class LoggerImpl : Logger {

	override fun v(tag: String, message: String) {
		Log.v(tag, message)
	}

	override fun i(tag: String, message: String) {
		Log.i(tag, message)
	}

	override fun d(tag: String, message: String) {
		Log.d(tag, message)
	}

	override fun w(tag: String, message: String) {
		Log.w(tag, message)
	}

	override fun w(tag: String, message: String, throwable: Throwable) {
		Log.w(tag, message, throwable)
	}

	override fun e(tag: String, message: String) {
		Log.e(tag, message)
	}

	override fun e(tag: String, message: String, throwable: Throwable) {
		Log.e(tag, message, throwable)
	}
}
