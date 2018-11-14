package com.garpr.android.extensions

import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.widget.TextViewCompat

fun TextView.clear() {
    text = ""
}

fun TextView.setTintedDrawableColor(@ColorInt color: Int) {
    val drawables = TextViewCompat.getCompoundDrawablesRelative(this)
    drawables[0] = drawables[0].setTintCompat(color)
    drawables[1] = drawables[1].setTintCompat(color)
    drawables[2] = drawables[2].setTintCompat(color)
    drawables[3] = drawables[3].setTintCompat(color)
    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(this, drawables[0],
            drawables[1], drawables[2], drawables[3])
}
