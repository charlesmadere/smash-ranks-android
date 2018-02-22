package com.garpr.android.misc

import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.v4.graphics.drawable.DrawableCompat
import java.text.DecimalFormat

object MiscUtils {

    private val DECIMAL_FORMAT = DecimalFormat("#.###")


    init {
        DECIMAL_FORMAT.minimumFractionDigits = 3
    }

    fun hashCode(vararg objects: Any?): Int {
        if (objects.isEmpty()) {
            return 0
        }

        if (objects.size == 1) {
            return objects[0]?.hashCode() ?: 0
        }

        var result = 1

        for (element in objects) {
            result = 31 * result + if (element == null) 0 else element.hashCode()
        }

        return result
    }

    fun tintDrawable(drawable: Drawable?, @ColorInt color: Int): Drawable? {
        return if (drawable == null) {
            null
        } else {
            val wrapper = DrawableCompat.wrap(drawable.mutate())
            DrawableCompat.setTint(wrapper, color)
            wrapper
        }
    }

    fun truncateFloat(value: Float): String {
        return DECIMAL_FORMAT.format(value.toDouble())
    }

}
