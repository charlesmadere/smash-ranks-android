package com.garpr.android.wrappers

interface CrashlyticsWrapper {

    fun initialize(disabled: Boolean)

    fun log(priority: Int, tag: String, msg: String)

    fun logException(tr: Throwable)

    fun setBool(key: String, value: Boolean)

    fun setInt(key: String, value: Int)

    fun setString(key: String, value: String)

}
