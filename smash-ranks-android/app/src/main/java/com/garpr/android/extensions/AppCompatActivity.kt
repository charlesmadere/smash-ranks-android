package com.garpr.android.extensions

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.subtitle(@StringRes subtitle: Int) {
    supportActionBar?.setSubtitle(subtitle)
}

var AppCompatActivity.subtitle: CharSequence?
    get() = supportActionBar?.subtitle
    set(subtitle) {
        supportActionBar?.subtitle = subtitle
    }
