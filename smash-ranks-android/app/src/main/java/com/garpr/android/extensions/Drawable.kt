package com.garpr.android.extensions

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

val Drawable.colorCompat: Int?
    get() = when (this) {
                is ColorDrawable -> color
                else -> null
            }

fun Drawable?.setTintCompat(@ColorInt color: Int): Drawable? {
    return if (this == null) {
        null
    } else {
        val wrapper = DrawableCompat.wrap(mutate())
        DrawableCompat.setTint(wrapper, color)
        wrapper
    }
}
