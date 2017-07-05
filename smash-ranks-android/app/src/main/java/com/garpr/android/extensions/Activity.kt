package com.garpr.android.extensions

import android.app.Activity

fun Activity.closeKeyboard() {
    inputMethodManager.closeKeyboard(this)
}
