package com.garpr.android.wrappers

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric

class CrashlyticsWrapperImpl(
        private val application: Application
) : CrashlyticsWrapper {

    private lateinit var crashlytics: Crashlytics


    override fun initialize(disabled: Boolean) {
        val crashlyticsCore = CrashlyticsCore.Builder()
                .disabled(disabled)
                .build()

        val crashlytics = Crashlytics.Builder()
                .core(crashlyticsCore)
                .build()

        Fabric.with(application, crashlytics)
        this.crashlytics = Crashlytics.getInstance()
    }

    override fun log(priority: Int, tag: String, msg: String) {
        crashlytics.core.log(priority, tag, msg)
    }

    override fun logException(tr: Throwable) {
        crashlytics.core.logException(tr)
    }

    override fun setBool(key: String, value: Boolean) {
        crashlytics.core.setBool(key, value)
    }

    override fun setInt(key: String, value: Int) {
        crashlytics.core.setInt(key, value)
    }

    override fun setString(key: String, value: String) {
        crashlytics.core.setString(key, value)
    }

}
