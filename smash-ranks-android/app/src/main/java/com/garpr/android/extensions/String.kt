package com.garpr.android.extensions

import java.net.URI

fun String?.toURI(): URI? {
    return if (isNullOrBlank()) {
        null
    } else {
        try {
            URI.create(this)
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
