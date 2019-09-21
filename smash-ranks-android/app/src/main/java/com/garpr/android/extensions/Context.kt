package com.garpr.android.extensions

import android.app.Activity
import android.app.ActivityManager
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.net.ConnectivityManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.FragmentActivity

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

val Context.connectivityManager: ConnectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

val Context.fragmentActivity: FragmentActivity?
    get() = activity as? FragmentActivity?

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

val Context.inputMethodManager: InputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

val Context.notificationManager: NotificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

val Context.notificationManagerCompat: NotificationManagerCompat
    get() = NotificationManagerCompat.from(this)

fun Context.requireActivity(): Activity {
    return activity ?: throw NullPointerException("Context ($this) is not attached to an Activity")
}

fun Context.requireFragmentActivity(): FragmentActivity {
    return fragmentActivity ?: throw RuntimeException("Context ($this) is not attached to a FragmentActivity")
}
