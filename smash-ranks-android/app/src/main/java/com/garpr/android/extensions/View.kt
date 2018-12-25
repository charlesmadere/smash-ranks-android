package com.garpr.android.extensions

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import com.garpr.android.dagger.AppComponent

val View.activity: Activity?
    get() = context.activity

val View.appComponent: AppComponent
    get() = context.appComponent

val View.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(context)

fun View.requireActivity(): Activity {
    return context.requireActivity()
}

fun View.requireFragmentActivity(): FragmentActivity {
    return context.requireFragmentActivity()
}

fun <T: View> View.requireViewByIdCompat(@IdRes id: Int): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        requireViewById(id)
    } else {
        findViewById(id) ?: throw IllegalArgumentException(
                "ID ${resources.getResourceEntryName(id)} does not reference a View inside this View")
    }
}

val View.verticalPositionInWindow: Int
    get() {
        val array = IntArray(2)
        getLocationInWindow(array)
        return array[1]
    }
