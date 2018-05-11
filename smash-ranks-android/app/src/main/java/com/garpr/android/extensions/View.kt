package com.garpr.android.extensions

import android.support.annotation.IdRes
import android.view.View
import com.garpr.android.dagger.AppComponent
import com.garpr.android.misc.DaggerUtils

val View.appComponent: AppComponent
    get() = DaggerUtils.getAppComponent(context)

fun <T : View> View.findViewByIdFromRoot(@IdRes id: Int): T? {
    return rootView.findViewById(id)
}

fun <T : View> View.requireViewByIdFromRoot(@IdRes id: Int): T {
    return findViewByIdFromRoot(id) ?: throw NullPointerException(
            "can't find view (${resources.getResourceName(id)})")
}

val View.verticalPositionInWindow: Int
    get() {
        val array = IntArray(2)
        getLocationInWindow(array)
        return array[1]
    }
