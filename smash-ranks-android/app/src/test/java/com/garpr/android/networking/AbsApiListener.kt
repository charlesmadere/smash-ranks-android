package com.garpr.android.networking

abstract class AbsApiListener<in T> : ApiListener<T> {

    override fun failure(errorCode: Int) {
        throw NotImplementedError()
    }

    override val isAlive = true

    override fun success(`object`: T?) {
        throw NotImplementedError()
    }

}
