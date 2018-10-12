package com.garpr.android.extensions

fun <T> ThreadLocal<T>.require(): T {
    return get() ?: throw NullPointerException()
}
