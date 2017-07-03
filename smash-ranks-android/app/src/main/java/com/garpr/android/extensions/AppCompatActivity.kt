package com.garpr.android.extensions

import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity

fun AppCompatActivity.subtitle(@StringRes subtitle: Int) {
    supportActionBar?.setSubtitle(subtitle)
}

var AppCompatActivity.subtitle: CharSequence?
    get() {
        return supportActionBar?.subtitle
    }
    set(value) {
        supportActionBar?.subtitle = value
    }
