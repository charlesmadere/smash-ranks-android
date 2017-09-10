package com.garpr.android.misc

import java.text.DecimalFormat



object MiscUtils {

    private val DECIMAL_FORMAT: DecimalFormat = DecimalFormat("#.###")


    init {
        DECIMAL_FORMAT.minimumFractionDigits = 3
    }

    fun integerCompare(x: Int, y: Int): Int {
        return if (x < y) -1 else if (x == y) 0 else 1
    }

    fun truncateFloat(value: Float): String {
        return DECIMAL_FORMAT.format(value.toDouble())
    }

}
