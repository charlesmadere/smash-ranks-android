package com.garpr.android.extensions

import com.squareup.moshi.JsonAdapter

fun <T : Any> JsonAdapter<T>.requireFromJson(string: String?): T {
    require(string != null) { "string parameter can't be null" }
    return fromJson(string) ?: throw NullPointerException("string ($string) returned null object")
}

fun <T : Any> JsonAdapter<T>.requireFromJsonValue(value: Any?): T {
    return fromJsonValue(value) ?: throw RuntimeException("unable to read in $value")
}
