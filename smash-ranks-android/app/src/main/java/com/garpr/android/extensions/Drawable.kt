package com.garpr.android.extensions

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable

val Drawable.colorCompat: Int?
    get() = when (this) {
                is ColorDrawable -> color
                else -> null
            }
