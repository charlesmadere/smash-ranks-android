package com.garpr.android.misc

import java.text.DecimalFormat

object MiscUtils {

    private val DECIMAL_FORMAT: DecimalFormat = DecimalFormat("#.###")


    init {
        DECIMAL_FORMAT.minimumFractionDigits = 3
    }

    fun truncateFloat(value: Float): String {
        return DECIMAL_FORMAT.format(value.toDouble())
    }

}
