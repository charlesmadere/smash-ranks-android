package com.garpr.android.extensions

import android.app.Activity
import android.app.ActivityManager
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.net.ConnectivityManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.FragmentActivity
import com.garpr.android.dagger.AppComponent
import com.garpr.android.dagger.AppComponentHandle

val Context.activity: Activity?
    get() {
        if (this is Activity) {
            return this
        }

        if (this is ContextWrapper) {
            var context = this

            do {
                context = (context as ContextWrapper).baseContext

                if (context is Activity) {
                    return context
                }
            } while (context is ContextWrapper)
        }

        return null
    }

val Context.activityManager: ActivityManager
    get() = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

val Context.appComponent: AppComponent
    get() {
        if (this is AppComponentHandle) {
            return appComponent
        }

        if (this is ContextWrapper) {
            var c = this

            do {
                c = (c as ContextWrapper).baseContext

                if (c is AppComponentHandle) {
                    return c.appComponent
                }
            } while (c is ContextWrapper)
        }

        val applicationContext = applicationContext

        if (applicationContext is AppComponentHandle) {
            return applicationContext.appComponent
        }

        throw RuntimeException("Context ($this) has no AppComponentHandle")
    }

val Context.connectivityManager: ConnectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

@ColorInt
@Throws(Resources.NotFoundException::class)
fun Context.getAttrColor(@AttrRes attrResId: Int): Int {
    val attrs = intArrayOf(attrResId)
    val ta = obtainStyledAttributes(attrs)

    if (!ta.hasValue(0)) {
        ta.recycle()
        throw Resources.NotFoundException("unable to find resId ($attrResId): " +
                resources.getResourceEntryName(attrResId))
    }

    val color = ta.getColor(0, 0)
    ta.recycle()

    return color
}

val Context.notificationManager: NotificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

val Context.notificationManagerCompat: NotificationManagerCompat
    get() = NotificationManagerCompat.from(this)

fun Context.requireActivity(): Activity {
    return activity ?: throw NullPointerException("Context ($this) is not attached to an Activity")
}

fun Context.requireFragmentActivity(): FragmentActivity {
    return requireActivity() as? FragmentActivity ?: throw RuntimeException("Context ($this) is not attached to a FragmentActivity")
}
