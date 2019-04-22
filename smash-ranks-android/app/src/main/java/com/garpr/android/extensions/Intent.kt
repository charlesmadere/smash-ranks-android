package com.garpr.android.extensions

import android.content.Intent
import android.os.Parcelable

fun Intent.putOptionalExtra(name: String, value: Parcelable?): Intent {
    if (value != null) {
        putExtra(name, value)
    }

    return this
}

fun Intent?.requireStringExtra(name: String): String {
    if (this == null) {
        throw NullPointerException("Intent is null")
    } else if (hasExtra(name)) {
        return getStringExtra(name) ?: throw NullPointerException("Intent String extra is null: \"$name\"")
    } else {
        throw NoSuchElementException("Intent does not have String extra: \"$name\"")
    }
}
