package com.garpr.android.extensions

import android.content.res.Resources
import androidx.annotation.IntegerRes

fun Resources.getLong(@IntegerRes id: Int): Long {
    return getInteger(id).toLong()
}
