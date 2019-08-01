package com.garpr.android.extensions

import android.os.Bundle
import android.os.Parcelable

fun Bundle?.optInt(key: String): Int? {
    if (this == null) {
        return null
    }

    if (containsKey(key)) {
        return getInt(key)
    }

    return null
}

fun Bundle.optPutInt(key: String, value: Int?) {
    if (value != null) {
        putInt(key, value)
    }
}

fun <T : Parcelable> Bundle?.requireParcelable(key: String): T {
    if (this == null) {
        throw NullPointerException("Bundle is null")
    } else if (containsKey(key)) {
        return getParcelable(key) ?: throw NullPointerException("Bundle Parcelable \"$key\" is null")
    } else {
        throw NoSuchElementException("Bundle does not contain Parcelable: \"$key\"")
    }
}

fun Bundle?.requireString(key: String): String {
    if (this == null) {
        throw NullPointerException("Bundle is null")
    } else if (containsKey(key)) {
        return getString(key) ?: throw NullPointerException("Bundle String \"$key\" is null")
    } else {
        throw NoSuchElementException("Bundle does not contain String: \"$key\"")
    }
}
