package com.garpr.android.wrappers

import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.garpr.android.BuildConfig
import com.garpr.android.misc.ThreadUtils

class WorkManagerWrapperImpl(
        private val context: Context,
        private val threadUtils: ThreadUtils
) : WorkManagerWrapper {

    override val configuration: Configuration
        get() = Configuration.Builder()
                .setExecutor(threadUtils.background)
                .setMinimumLoggingLevel(if (BuildConfig.DEBUG) Log.VERBOSE else Log.INFO)
                .setTaskExecutor(threadUtils.background)
                .build()

    private val workManager: WorkManager
        get() = WorkManager.getInstance(context)

    override fun cancelAllWorkByTag(tag: String) {
        workManager.cancelAllWorkByTag(tag)
    }

    override fun enqueue(workRequest: WorkRequest) {
        workManager.enqueue(workRequest)
    }

}
