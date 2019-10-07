package com.garpr.android.extensions

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.core.view.ViewCompat

val View.activity: Activity?
    get() = context.activity

val View.inputMethodManager: InputMethodManager
    get() = context.inputMethodManager

val View.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(context)

fun View.requestFocusAndOpenKeyboard() {
    requestFocus()
    inputMethodManager.toggleSoftInputFromWindow(windowToken, InputMethodManager.SHOW_FORCED, 0)
}

fun View.requireActivity(): Activity {
    return context.requireActivity()
}

fun <T : View> View.requireViewByIdCompat(@IdRes id: Int): T {
    return ViewCompat.requireViewById(this, id)
}

val View.verticalPositionInWindow: Int
    get() {
        val array = IntArray(2)
        getLocationInWindow(array)
        return array[1]
    }
