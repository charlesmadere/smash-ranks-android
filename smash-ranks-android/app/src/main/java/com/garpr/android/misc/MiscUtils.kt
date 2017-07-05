package com.garpr.android.misc

import java.text.DecimalFormat

class MiscUtils {

    companion object {
        private val DECIMAL_FORMAT: DecimalFormat

        init {
            DECIMAL_FORMAT = DecimalFormat("#.###")
            DECIMAL_FORMAT.minimumFractionDigits = 3
        }

        fun truncateFloat(value: Float): String {
            return DECIMAL_FORMAT.format(value.toDouble())
        }
    }

}
