package com.garpr.android.extensions

import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.widget.TextViewCompat
import com.garpr.android.misc.MiscUtils

fun TextView.clear() {
    text = ""
}

fun TextView.setTintedDrawableColor(@ColorInt color: Int) {
    val drawables = TextViewCompat.getCompoundDrawablesRelative(this)
    drawables[0] = MiscUtils.tintDrawable(drawables[0], color)
    drawables[1] = MiscUtils.tintDrawable(drawables[1], color)
    drawables[2] = MiscUtils.tintDrawable(drawables[2], color)
    drawables[3] = MiscUtils.tintDrawable(drawables[3], color)
    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(this, drawables[0],
            drawables[1], drawables[2], drawables[3])
}
