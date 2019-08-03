package com.garpr.android.wrappers

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.garpr.android.BuildConfig
import com.garpr.android.misc.ThreadUtils

class WorkManagerWrapperImpl(
        private val application: Application,
        private val threadUtils: ThreadUtils
) : WorkManagerWrapper {

    private val workManager: WorkManager
        get() = WorkManager.getInstance(application)

    override val configuration: Configuration
        get() = Configuration.Builder()
                .setExecutor(threadUtils.background)
                .setMinimumLoggingLevel(if (BuildConfig.DEBUG) Log.VERBOSE else Log.INFO)
                .build()

    override fun cancelAllWorkByTag(tag: String) {
        workManager.cancelAllWorkByTag(tag)
    }

    override fun enqueue(workRequest: WorkRequest) {
        workManager.enqueue(workRequest)
    }

}
