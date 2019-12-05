package com.garpr.android.extensions

fun <K, V: Any> Map<K, V?>?.require(key: K): V {
    checkNotNull(this) { "map itself is null" }

    if (key in this) {
        return get(key) ?: throw NullPointerException("item at key $key is null")
    }

    throw NoSuchElementException("no item with key $key exists")
}
