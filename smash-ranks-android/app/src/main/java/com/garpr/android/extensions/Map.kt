package com.garpr.android.extensions

fun <K, V> Map<K, V>?.require(key: K): V {
    return if (this == null) {
        throw NullPointerException()
    } else if (key in this) {
        get(key) ?: throw NullPointerException("item at key $key is null")
    } else {
        throw NoSuchElementException("no item with key $key exists")
    }
}
