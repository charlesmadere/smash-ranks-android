package com.garpr.android.misc

import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import java.text.DecimalFormat
import java.text.NumberFormat

object MiscUtils {

    private val DECIMAL_FORMAT: NumberFormat = DecimalFormat("#.###")


    init {
        DECIMAL_FORMAT.minimumFractionDigits = 3
    }

    @ColorInt
    fun brightenOrDarkenColor(@ColorInt color: Int, factor: Float): Int {
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(color, hsl)
        hsl[2] = hsl[2] * factor
        return ColorUtils.HSLToColor(hsl)
    }

    @ColorInt
    fun brightenOrDarkenColorIfLightnessIs(@ColorInt color: Int, factor: Float, lightness: Float): Int {
        return if (isColorLightness(color, lightness)) {
            brightenOrDarkenColor(color, factor)
        } else {
            color
        }
    }

    fun hashCode(vararg objects: Any?): Int {
        if (objects.isNullOrEmpty()) {
            return 0
        }

        if (objects.size == 1) {
            return objects[0]?.hashCode() ?: 0
        }

        var result = 1

        for (element in objects) {
            result = 31 * result + (element?.hashCode() ?: 0)
        }

        return result
    }

    fun isColorLightness(@ColorInt color: Int, lightness: Float): Boolean {
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(color, hsl)
        return hsl[2] >= lightness
    }

    fun truncateFloat(value: Float): String {
        return DECIMAL_FORMAT.format(value.toDouble())
    }

}
