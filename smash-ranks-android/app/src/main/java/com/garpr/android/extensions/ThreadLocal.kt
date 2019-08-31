package com.garpr.android.extensions

fun <T : Any> ThreadLocal<T>.require(): T {
    return requireNotNull(get())
}
