package com.garpr.android.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.garpr.android.misc.MiscUtils

fun ImageView.clear() {
    setImageDrawable(null)
}

fun ImageView.setTintedImageColor(@ColorInt color: Int) {
    setTintedImageDrawable(drawable, color)
}

fun ImageView.setTintedImageDrawable(drawable: Drawable?, @ColorInt color: Int) {
    setImageDrawable(MiscUtils.tintDrawable(drawable, color))
}

fun ImageView.setTintedImageResource(@DrawableRes drawable: Int, @ColorInt color: Int) {
    setTintedImageDrawable(ContextCompat.getDrawable(context, drawable), color)
}
