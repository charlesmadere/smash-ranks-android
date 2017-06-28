package com.garpr.android.misc

import android.app.Application

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore

import io.fabric.sdk.android.Fabric

class CrashlyticsWrapperImpl(
        private val mApplication: Application
) : CrashlyticsWrapper {

    override fun initialize(disabled: Boolean) {
        val crashlyticsCore = CrashlyticsCore.Builder()
                .disabled(disabled)
                .build()

        val crashlytics = Crashlytics.Builder()
                .core(crashlyticsCore)
                .build()

        Fabric.with(mApplication, crashlytics)
    }

    override fun log(priority: Int, tag: String, msg: String) {
        Crashlytics.log(priority, tag, msg)
    }

    override fun logException(tr: Throwable) {
        Crashlytics.logException(tr)
    }

    override fun setBool(key: String, value: Boolean) {
        Crashlytics.setBool(key, value)
    }

    override fun setInt(key: String, value: Int) {
        Crashlytics.setInt(key, value)
    }

    override fun setString(key: String, value: String) {
        Crashlytics.setString(key, value)
    }

}
