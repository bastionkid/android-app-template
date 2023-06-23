package com.azuredragon.core.log

interface Logger {

    fun v(tag: String?, message: String)

    fun i(tag: String?, message: String)

    fun d(tag: String?, message: String)

    fun w(tag: String?, message: String)

    fun w(tag: String?, message: String, throwable: Throwable)

    fun e(tag: String?, message: String, throwable: Throwable? = null)
}