package com.garpr.android.extensions

import android.app.Activity
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.garpr.android.dagger.AppComponent

val View.activity: Activity?
    get() = context.activity

val View.appComponent: AppComponent
    get() = context.appComponent

fun View.requireActivity(): Activity {
    return context.requireActivity()
}

fun View.requireFragmentActivity(): FragmentActivity {
    return context.requireFragmentActivity()
}

val View.verticalPositionInWindow: Int
    get() {
        val array = IntArray(2)
        getLocationInWindow(array)
        return array[1]
    }
