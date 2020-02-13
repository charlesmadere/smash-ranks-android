package com.garpr.android.extensions

fun Char?.safeEquals(other: Char?, ignoreCase: Boolean = false): Boolean {
    return if (this != null && other != null) {
        equals(other, ignoreCase)
    } else {
        this == null && other == null
    }
}
