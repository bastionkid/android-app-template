package com.azuredragon.core.common

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

fun Map<String, Any?>.toBundle(): Bundle {
	return bundleOf(this.entries)
}

/**
 * Copied from androidx.core.os Bundle.kt file as we don't want to bring Android specific
 * dependencies into this module
 */
fun bundleOf(entrySet: Set<Map.Entry<String, Any?>>): Bundle = Bundle(entrySet.size).apply {
	for ((key, value) in entrySet) {
		when (value) {
			null -> putString(key, null) // Any nullable type will suffice.

			// Scalars
			is Boolean -> putBoolean(key, value)
			is Byte -> putByte(key, value)
			is Char -> putChar(key, value)
			is Double -> putDouble(key, value)
			is Float -> putFloat(key, value)
			is Int -> putInt(key, value)
			is Long -> putLong(key, value)
			is Short -> putShort(key, value)

			// References
			is Bundle -> putBundle(key, value)
			is CharSequence -> putCharSequence(key, value)
			is Parcelable -> putParcelable(key, value)

			// Scalar arrays
			is BooleanArray -> putBooleanArray(key, value)
			is ByteArray -> putByteArray(key, value)
			is CharArray -> putCharArray(key, value)
			is DoubleArray -> putDoubleArray(key, value)
			is FloatArray -> putFloatArray(key, value)
			is IntArray -> putIntArray(key, value)
			is LongArray -> putLongArray(key, value)
			is ShortArray -> putShortArray(key, value)

			// Reference arrays
			is Array<*> -> {
				val componentType = value::class.java.componentType!!
				@Suppress("UNCHECKED_CAST") // Checked by reflection.
				when {
					Parcelable::class.java.isAssignableFrom(componentType) -> {
						putParcelableArray(key, value as Array<Parcelable>)
					}
					String::class.java.isAssignableFrom(componentType) -> {
						putStringArray(key, value as Array<String>)
					}
					CharSequence::class.java.isAssignableFrom(componentType) -> {
						putCharSequenceArray(key, value as Array<CharSequence>)
					}
					Serializable::class.java.isAssignableFrom(componentType) -> {
						putSerializable(key, value)
					}
					else -> {
						val valueType = componentType.canonicalName
						throw IllegalArgumentException(
							"Illegal value array type $valueType for key \"$key\"",
						)
					}
				}
			}
			// Last resort. Also we must check this after Array<*> as all arrays are serializable.
			is Serializable -> putSerializable(key, value)
			else -> {
				val valueType = value.javaClass.canonicalName
				throw IllegalArgumentException("Illegal value type $valueType for key \"$key\"")
			}
		}
	}
}
