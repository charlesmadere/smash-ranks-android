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
    checkNotNull(this) { "Bundle is null" }

    if (containsKey(key)) {
        return requireNotNull(getParcelable(key)) { "Bundle Parcelable \"$key\" is null" }
    }

    throw NoSuchElementException("Bundle does not contain Parcelable: \"$key\"")
}

fun Bundle?.requireString(key: String): String {
    checkNotNull(this) { "Bundle is null" }

    if (containsKey(key)) {
        return requireNotNull(getString(key)) { "Bundle String \"$key\" is null" }
    }

    throw NoSuchElementException("Bundle does not contain String: \"$key\"")
}
