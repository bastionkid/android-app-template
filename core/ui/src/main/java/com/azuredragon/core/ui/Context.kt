package com.azuredragon.core.ui

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

fun Context.showToast(text: CharSequence) {
	Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.showToastLong(text: CharSequence) {
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
