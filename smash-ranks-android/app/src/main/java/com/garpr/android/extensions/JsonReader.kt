package com.garpr.android.extensions

import com.squareup.moshi.JsonReader

@Suppress("UNCHECKED_CAST")
fun JsonReader.readJsonValueMap(): Map<String, Any>? {
    return if (hasNext() && peek() != JsonReader.Token.NULL) {
        readJsonValue() as? Map<String, Any>?
    } else {
        null
    }
}
