package com.garpr.android.extensions

import android.os.Build
import android.view.Window

val Window.statusBarColorCompat: Int?
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                statusBarColor
            } else {
                null
            }
