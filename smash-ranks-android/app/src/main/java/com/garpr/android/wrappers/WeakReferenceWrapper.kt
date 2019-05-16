package com.garpr.android.wrappers

import java.lang.ref.WeakReference
import java.util.Objects

class WeakReferenceWrapper<T>(referent: T?) : WeakReference<T?>(referent) {

    override fun equals(other: Any?): Boolean {
        return other is WeakReference<*> && other.get() == get()
    }

    override fun hashCode(): Int = Objects.hashCode(get())

}
