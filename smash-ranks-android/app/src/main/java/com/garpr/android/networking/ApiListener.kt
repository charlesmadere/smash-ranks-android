package com.garpr.android.networking

import com.garpr.android.misc.Heartbeat

interface ApiListener<T> : Heartbeat {

    fun failure(errorCode: Int)

    fun success(`object`: T?)

}
