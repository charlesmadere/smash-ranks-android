package com.garpr.android.extensions

import java.text.DecimalFormat
import java.text.NumberFormat

private val DECIMAL_FORMAT: NumberFormat = DecimalFormat("#.###").apply {
    maximumFractionDigits = 3
    minimumFractionDigits = 3
}

fun Number.truncate(): String {
    return DECIMAL_FORMAT.format(toDouble())
}
