package com.garpr.android.misc

import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils

object ColorUtils {

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

    fun isColorLightness(@ColorInt color: Int, lightness: Float): Boolean {
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(color, hsl)
        return hsl[2] >= lightness
    }

}
