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
    checkNotNull(this) { "Intent is null" }

    if (hasExtra(name)) {
        return requireNotNull(getStringExtra(name)) { "Intent String extra is null: \"$name\"" }
    }

    throw NoSuchElementException("Intent does not have String extra: \"$name\"")
}
