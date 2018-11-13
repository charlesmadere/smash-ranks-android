package com.garpr.android.wrappers

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.garpr.android.misc.Timber

class WorkManagerWrapperImpl(
        private val application: Application,
        private val timber: Timber
) : WorkManagerWrapper {

    companion object {
        private const val TAG = "WorkManagerWrapperImpl"
    }

    override fun cancelAllWorkByTag(tag: String) {
        workManager.cancelAllWorkByTag(tag)
    }

    override fun enqueue(workRequest: WorkRequest) {
        workManager.enqueue(workRequest)
    }

    override fun initialize() {
        timber.d(TAG, "Initializing WorkManager...")
        WorkManager.initialize(application, Configuration.Builder().build())
        timber.d(TAG, "WorkManager has been initialized")
    }

    private val workManager: WorkManager
        get() = WorkManager.getInstance()

}
