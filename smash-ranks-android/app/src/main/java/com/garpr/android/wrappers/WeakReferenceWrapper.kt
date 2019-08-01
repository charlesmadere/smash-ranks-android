package com.garpr.android.wrappers

import androidx.core.util.ObjectsCompat
import java.lang.ref.WeakReference

class WeakReferenceWrapper<T>(referent: T?) : WeakReference<T?>(referent) {

    override fun equals(other: Any?): Boolean {
        return other is WeakReference<*> && other.get() == get()
    }

    override fun hashCode(): Int = ObjectsCompat.hashCode(get())

}
