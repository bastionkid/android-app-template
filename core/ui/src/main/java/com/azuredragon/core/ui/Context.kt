package com.azuredragon.core.ui

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService

fun Context.toast(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.toastLong(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

@ColorInt
fun Context.getTransparentColor(): Int {
    return ContextCompat.getColor(this, android.R.color.transparent)
}

fun Context.getActivity(): AppCompatActivity = when (this) {
    is AppCompatActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> throw Exception("Activity not found")
}

fun Context.vibrate(pattern: LongArray) {
    val vibrator = getSystemService<Vibrator>()

    if (vibrator?.hasVibrator() == true) {
        if (Build.VERSION.SDK_INT >= 26)
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
        else
            vibrator.vibrate(pattern, -1)
    }
}