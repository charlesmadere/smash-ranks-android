package com.garpr.android.extensions

import android.os.Bundle
import android.os.Parcelable

fun <T : Parcelable> Bundle?.requireParcelable(key: String): T {
    checkNotNull(this) { "Bundle is null" }

    if (containsKey(key)) {
        return getParcelable(key) ?: throw NullPointerException(
                "Bundle Parcelable \"$key\" is null")
    }

    throw NoSuchElementException("Bundle does not contain Parcelable: \"$key\"")
}

fun Bundle?.requireString(key: String): String {
    checkNotNull(this) { "Bundle is null" }

    if (containsKey(key)) {
        return getString(key) ?: throw NullPointerException("Bundle String \"$key\" is null")
    }

    throw NoSuchElementException("Bundle does not contain String: \"$key\"")
}
