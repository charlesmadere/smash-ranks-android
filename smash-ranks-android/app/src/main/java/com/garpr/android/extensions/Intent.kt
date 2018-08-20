package com.garpr.android.extensions

import android.content.Intent
import android.os.Parcelable

fun Intent.putOptionalExtra(name: String, value: Int?): Intent {
    if (value != null) {
        putExtra(name, value)
    }

    return this
}

fun Intent.putOptionalExtra(name: String, value: Parcelable?): Intent {
    if (value != null) {
        putExtra(name, value)
    }

    return this
}

fun Intent?.requireStringExtra(name: String): String {
    if (this == null) {
        throw NullPointerException("Intent is null")
    }

    if (!hasExtra(name)) {
        throw RuntimeException("Intent does not have String extra: \"$name\"")
    }

    return getStringExtra(name) ?: throw NullPointerException("Intent String extra is null: \"$name\"")
}
