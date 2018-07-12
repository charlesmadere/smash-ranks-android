package com.garpr.android.extensions

import android.content.Intent

fun Intent?.requireStringExtra(name: String): String {
    if (this == null) {
        throw NullPointerException("Intent is null")
    }

    if (!hasExtra(name)) {
        throw RuntimeException("Intent does not have String extra \"$name\"")
    }

    return getStringExtra(name) ?: throw NullPointerException("Intent String extra \"$name\" is null")
}
