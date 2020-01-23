package com.garpr.android.misc

import android.util.Log

class StackTraceUtilsImpl : StackTraceUtils {

    override fun toString(throwable: Throwable?): String {
        return if (throwable == null) {
            ""
        } else {
            Log.getStackTraceString(throwable)
        }
    }

}
