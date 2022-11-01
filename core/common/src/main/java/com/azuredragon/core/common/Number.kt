package com.azuredragon.core.common

fun Number.formatAmountToString(): String = when (this) {
    is Int -> {
        this.toString()
    }
    is Double,
    is Float -> {
        String.format("%.1f", this)
    }
    else -> {
        throw IllegalArgumentException("Unsupported amount Number type received.")
    }
}