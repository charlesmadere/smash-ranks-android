package com.garpr.android.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.view.inputmethod.InputMethodManager

fun Context.getActivity(): Activity {
    return optActivity() ?: throw NullPointerException(
            "Context ($this) is not attached to an Activity")
}

@ColorInt
@Throws(Resources.NotFoundException::class)
fun Context.getAttrColor(@AttrRes attrResId: Int): Int {
    val attrs = intArrayOf(attrResId)
    val ta = obtainStyledAttributes(attrs)

    if (!ta.hasValue(0)) {
        ta.recycle()
        throw Resources.NotFoundException("unable to find resId ($attrResId): " +
                resources.getResourceEntryName(attrResId))
    }

    val color = ta.getColor(0, 0)
    ta.recycle()

    return color
}

val Context.inputMethodManager: InputMethodManager
    get() {
        return getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

fun Context.optActivity(): Activity? {
    if (this is Activity) {
        return this
    }

    if (this is ContextWrapper) {
        var context = baseContext

        do {
            context = (context as ContextWrapper).baseContext

            if (context is Activity) {
                return context
            }
        } while (context is ContextWrapper)
    }

    return null
}
