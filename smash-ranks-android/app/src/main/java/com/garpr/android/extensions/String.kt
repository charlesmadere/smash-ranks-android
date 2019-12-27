package com.garpr.android.extensions

import java.net.URI as JavaUri

fun String?.toURI(): JavaUri? {
    return if (isNullOrBlank()) {
        null
    } else {
        try {
            JavaUri.create(this)
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
