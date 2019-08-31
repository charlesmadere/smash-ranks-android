package com.garpr.android.extensions

import com.squareup.moshi.JsonAdapter

fun <T : Any> JsonAdapter<T>.requireFromJson(string: String?): T {
    require(string != null) { "string parameter can't be null" }
    return requireNotNull(fromJson(string)) { "string ($string) returned null object" }
}

fun <T : Any> JsonAdapter<T>.requireFromJsonValue(value: Any?): T {
    return requireNotNull(fromJsonValue(value)) { "unable to read in $value" }
}
