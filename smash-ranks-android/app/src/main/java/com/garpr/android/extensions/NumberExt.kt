package com.garpr.android.extensions

import java.text.NumberFormat

private val TRUNCATE_DECIMAL_FORMAT: NumberFormat = NumberFormat.getInstance().apply {
    maximumFractionDigits = 3
    minimumFractionDigits = 3
}

fun Number.truncate(): String {
    return TRUNCATE_DECIMAL_FORMAT.format(toDouble())
}
