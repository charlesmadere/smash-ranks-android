package com.garpr.android.extensions

import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.Window
import com.garpr.android.R

var Window.statusBarColorCompat: Int?
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                statusBarColor
            } else {
                null
            }
    set(value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statusBarColor = value ?: ContextCompat.getColor(context, R.color.transparent)
        }
    }
