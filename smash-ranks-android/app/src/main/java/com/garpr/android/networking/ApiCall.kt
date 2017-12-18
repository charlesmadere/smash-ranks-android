package com.garpr.android.networking

import java.lang.ref.WeakReference

class ApiCall<in T>(listener: ApiListener<T>) : ApiListener<T> {

    private val mReference = WeakReference<ApiListener<T>>(listener)


    override fun failure(errorCode: Int) {
        val listener = mReference.get()

        if (listener != null && listener.isAlive) {
            listener.failure(errorCode)
        }
    }

    override val isAlive: Boolean
        get() {
            val listener = mReference.get()
            return listener != null && listener.isAlive
        }

    override fun success(`object`: T?) {
        val listener = mReference.get()

        if (listener != null && listener.isAlive) {
            listener.success(`object`)
        }
    }

}
