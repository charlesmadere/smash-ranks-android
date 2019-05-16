package com.garpr.android.misc

import androidx.annotation.AttrRes

import com.garpr.android.R

interface Timber {

    fun clearEntries()

    fun d(tag: String, msg: String, tr: Throwable? = null)

    fun e(tag: String, msg: String, tr: Throwable? = null)

    val entries: List<Entry>

    fun w(tag: String, msg: String, tr: Throwable? = null)


    abstract class Entry protected constructor(
            @AttrRes val color: Int,
            val tag: String,
            val msg: String,
            val stackTrace: String? = null
    )

    class DebugEntry internal constructor(
            tag: String,
            msg: String,
            stackTrace: String? = null
    ) : Entry(R.attr.timberDebug, tag, msg, stackTrace)

    class ErrorEntry internal constructor(
            tag: String,
            msg: String,
            stackTrace: String? = null
    ) : Entry(R.attr.timberError, tag, msg, stackTrace)

    class WarnEntry internal constructor(
            tag: String,
            msg: String,
            stackTrace: String? = null
    ) : Entry(R.attr.timberWarn, tag, msg, stackTrace)

}
