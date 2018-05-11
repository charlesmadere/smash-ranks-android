package com.garpr.android.misc

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import com.garpr.android.dagger.AppComponent
import com.garpr.android.dagger.AppComponentHandle

object DaggerUtils {

    fun getAppComponent(context: Context): AppComponent {
        if (context is AppComponentHandle) {
            return context.appComponent
        }

        if (context is ContextWrapper) {
            var c = context

            do {
                c = (c as ContextWrapper).baseContext

                if (c is AppComponentHandle) {
                    return c.appComponent
                }
            } while (c is ContextWrapper)
        }

        val applicationContext = context.applicationContext

        if (applicationContext is AppComponentHandle) {
            return applicationContext.appComponent
        }

        throw RuntimeException("Context ($context) has no AppComponentHandle")
    }

    fun getAppComponent(view: View): AppComponent {
        return getAppComponent(view.context)
    }

}
