package com.garpr.android.misc

import android.support.annotation.AttrRes

import com.garpr.android.R

interface Timber {

    fun clearEntries()

    fun d(tag: String, msg: String)

    fun d(tag: String, msg: String, tr: Throwable?)

    fun e(tag: String, msg: String)

    fun e(tag: String, msg: String, tr: Throwable?)

    val entries: List<Entry>

    fun w(tag: String, msg: String)

    fun w(tag: String, msg: String, tr: Throwable?)


    abstract class Entry protected constructor(
            @AttrRes val color: Int,
            val tag: String,
            val msg: String,
            val stackTrace: String?
    )

    class DebugEntry internal constructor(
            tag: String,
            msg: String,
            stackTrace: String?
    ) : Entry(R.attr.timberDebug, tag, msg, stackTrace)

    class ErrorEntry internal constructor(
            tag: String,
            msg: String,
            stackTrace: String?
    ) : Entry(R.attr.timberError, tag, msg, stackTrace)

    class WarnEntry internal constructor(
            tag: String,
            msg: String,
            stackTrace: String?
    ) : Entry(R.attr.timberWarn, tag, msg, stackTrace)

}
