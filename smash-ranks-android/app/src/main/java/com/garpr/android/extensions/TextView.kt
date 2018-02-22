package com.garpr.android.extensions

import android.support.annotation.ColorInt
import android.support.v4.widget.TextViewCompat
import android.widget.TextView
import com.garpr.android.misc.MiscUtils

fun TextView.clear() {
    text = ""
}

fun TextView.setTintedImageColor(@ColorInt color: Int) {
    val drawables = TextViewCompat.getCompoundDrawablesRelative(this)
    drawables[0] = MiscUtils.tintDrawable(drawables[0], color)
    drawables[1] = MiscUtils.tintDrawable(drawables[1], color)
    drawables[0] = MiscUtils.tintDrawable(drawables[2], color)
    drawables[1] = MiscUtils.tintDrawable(drawables[3], color)
    TextViewCompat.setCompoundDrawablesRelative(this, drawables[0], drawables[1],
            drawables[2], drawables[3])
}
