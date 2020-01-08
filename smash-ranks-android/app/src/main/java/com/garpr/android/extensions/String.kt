package com.garpr.android.extensions

import java.net.URI as JavaUri

fun String?.toJavaUri(): JavaUri? {
    return if (this.isNullOrBlank()) {
        null
    } else {
        try {
            JavaUri.create(this)
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
