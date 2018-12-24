package com.garpr.android.extensions

import android.app.Activity
import android.view.View

fun Activity.hideKeyboard() {
    val view = currentFocus ?: View(this)
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
