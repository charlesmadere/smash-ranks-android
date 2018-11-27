package com.garpr.android.extensions

import android.os.Bundle
import android.os.Parcelable

fun Bundle.putOptionalCharSequence(key: String, value: CharSequence?) {
    if (value != null) {
        putCharSequence(key, value)
    }
}

fun Bundle?.requireCharSequence(key: String): CharSequence {
    if (this == null) {
        throw NullPointerException("Bundle is null")
    }

    return getCharSequence(key) ?: throw NullPointerException("CharSequence \"$key\" doesn't exist")
}

fun <T : Parcelable> Bundle?.requireParcelable(key: String): T {
    if (this == null) {
        throw NullPointerException("Bundle is null")
    }

    return getParcelable(key) ?: throw NullPointerException("Parcelable \"$key\" doesn't exist")
}

fun Bundle?.requireString(key: String): String {
    if (this == null) {
        throw NullPointerException("Bundle is null")
    }

    return getString(key) ?: throw NullPointerException("String \"$key\" doesn't exist")
}
