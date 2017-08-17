package com.garpr.android.extensions

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

fun InputMethodManager.closeKeyboard(activity: Activity) {
    var view = activity.currentFocus

    if (view == null) {
        view = View(activity)
    }

    closeKeyboard(view)
}

fun InputMethodManager.closeKeyboard(view: View) {
    view.clearFocus()
    hideSoftInputFromWindow(view.windowToken, 0)
}

fun InputMethodManager.openKeyboard(view: View) {
    view.requestFocus()
    showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}
