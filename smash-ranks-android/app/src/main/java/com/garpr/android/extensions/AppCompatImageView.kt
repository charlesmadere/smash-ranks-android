package com.garpr.android.extensions

import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.AppCompatImageView

fun AppCompatImageView.setTintedImageDrawable(@ColorInt color: Int) {
    setTintedImageDrawable(drawable, color)
}

fun AppCompatImageView.setTintedImageDrawable(drawable: Drawable?, @ColorInt color: Int) {
    if (drawable == null) {
        setImageDrawable(null)
        return
    }

    val wrapper = DrawableCompat.wrap(drawable)
    DrawableCompat.setTint(wrapper, color)
    setImageDrawable(drawable)
}

fun AppCompatImageView.setTintedImageResource(@DrawableRes drawable: Int, @ColorInt color: Int) {
    setTintedImageDrawable(ContextCompat.getDrawable(context, drawable), color)
}
