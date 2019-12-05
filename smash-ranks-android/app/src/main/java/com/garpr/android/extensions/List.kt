package com.garpr.android.extensions

fun <T : Any> List<T?>?.require(index: Int): T {
    checkNotNull(this) { "list itself is null" }
    return get(index) ?: throw NullPointerException("item at index $index is null")
}
