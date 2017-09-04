package com.garpr.android.extensions

import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity

fun AppCompatActivity.subtitle(@StringRes subtitle: Int) {
    supportActionBar?.setSubtitle(subtitle)
}

var AppCompatActivity.subtitle: CharSequence?
    get() = supportActionBar?.subtitle
    set(subtitle) {
        supportActionBar?.subtitle = subtitle
    }
