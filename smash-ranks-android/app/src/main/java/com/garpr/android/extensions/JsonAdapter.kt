package com.garpr.android.extensions

import com.squareup.moshi.JsonAdapter

fun <T> JsonAdapter<T>.requireFromJson(string: String?): T {
    return if (string == null) {
        throw IllegalArgumentException("string parameter can't be null")
    } else {
        fromJson(string) ?: throw NullPointerException("string ($string) returned null object")
    }
}

fun <T> JsonAdapter<T>.requireFromJsonValue(value: Any?): T {
    return fromJsonValue(value) ?: throw NullPointerException("unable to read in $value")
}
