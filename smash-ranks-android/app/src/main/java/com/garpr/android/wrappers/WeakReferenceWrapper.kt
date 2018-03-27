package com.garpr.android.wrappers

import com.garpr.android.misc.MiscUtils
import java.lang.ref.WeakReference

class WeakReferenceWrapper<T>(referent: T?) : WeakReference<T>(referent) {

    override fun equals(other: Any?): Boolean {
        return other is WeakReference<*> && other.get() == get()
    }

    override fun hashCode(): Int {
        return MiscUtils.hashCode(get())
    }

}
