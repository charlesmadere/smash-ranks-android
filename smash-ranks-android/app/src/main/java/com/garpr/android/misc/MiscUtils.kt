package com.garpr.android.misc

import android.os.Build
import android.support.annotation.RequiresApi
import java.text.DecimalFormat
import java.util.*

object MiscUtils {

    private val DECIMAL_FORMAT: DecimalFormat = DecimalFormat("#.###")
    private val IMPL = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) Api19Impl() else BaseImpl()


    init {
        DECIMAL_FORMAT.minimumFractionDigits = 3
    }

    private interface Impl {
        fun hash(vararg objects: Any?): Int
    }

    private open class BaseImpl : Impl {
        override fun hash(vararg objects: Any?): Int {
            return Arrays.hashCode(objects)
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private class Api19Impl : BaseImpl() {
        override fun hash(vararg objects: Any?): Int {
            return Objects.hash(objects)
        }
    }

    fun hash(vararg objects: Any?): Int {
        return IMPL.hash(objects)
    }

    fun truncateFloat(value: Float): String {
        return DECIMAL_FORMAT.format(value.toDouble())
    }

}
