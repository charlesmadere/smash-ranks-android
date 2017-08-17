package com.garpr.android.networking

import com.garpr.android.misc.Constants
import com.garpr.android.misc.Heartbeat

interface ApiListener<in T> : Heartbeat {

    fun failure(errorCode: Int = Constants.ERROR_CODE_UNKNOWN)

    fun success(`object`: T?)

}
