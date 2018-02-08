package com.garpr.android.extensions

import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.widget.ImageView

fun ImageView.clear() {
    setImageDrawable(null)
}

fun ImageView.setTintedImageDrawable(@ColorInt color: Int) {
    setTintedImageDrawable(drawable, color)
}

fun ImageView.setTintedImageDrawable(drawable: Drawable?, @ColorInt color: Int) {
    setImageDrawable(if (drawable == null) {
        null
    } else {
        val wrapper = DrawableCompat.wrap(drawable.mutate())
        DrawableCompat.setTint(wrapper, color)
        wrapper
    })
}

fun ImageView.setTintedImageResource(@DrawableRes drawable: Int, @ColorInt color: Int) {
    setTintedImageDrawable(ContextCompat.getDrawable(context, drawable), color)
}
