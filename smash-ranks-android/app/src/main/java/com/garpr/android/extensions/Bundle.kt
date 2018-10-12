package com.garpr.android.extensions

import android.os.Bundle
import android.os.Parcelable

fun <T : Parcelable> Bundle.requireParcelable(key: String): T {
    return getParcelable(key) ?: throw NullPointerException("Parcelable \"$key\" doesn't exist")
}

fun Bundle.requireString(key: String): String {
    return getString(key) ?: throw NullPointerException("String \"$key\" doesn't exist")
}
