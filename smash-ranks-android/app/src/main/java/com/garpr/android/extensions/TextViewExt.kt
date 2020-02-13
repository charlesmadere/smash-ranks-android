package com.garpr.android.extensions

import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.widget.TextViewCompat

fun TextView.clear() {
    text = ""
}

var TextView.compoundDrawablesRelativeCompat: Array<Drawable?>
    get() = TextViewCompat.getCompoundDrawablesRelative(this)
    set(value) = TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(this,
            value[0], value[1], value[2], value[3])

fun TextView.setTintedDrawableColor(@ColorInt color: Int) {
    val drawables = compoundDrawablesRelativeCompat
    drawables[0] = drawables[0].setTintCompat(color)
    drawables[1] = drawables[1].setTintCompat(color)
    drawables[2] = drawables[2].setTintCompat(color)
    drawables[3] = drawables[3].setTintCompat(color)
    compoundDrawablesRelativeCompat = drawables
}
