package com.garpr.android.extensions

fun <T> List<T>?.require(index: Int): T {
    return if (this == null) {
        throw NullPointerException()
    } else {
        get(index) ?: throw NullPointerException("item at index $index is null")
    }
}
