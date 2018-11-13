package com.garpr.android.wrappers

import androidx.work.WorkManager
import androidx.work.WorkRequest

class WorkManagerWrapperImpl : WorkManagerWrapper {

    override fun cancelAllWorkByTag(tag: String) {
        workManager.cancelAllWorkByTag(tag)
    }

    override fun enqueue(workRequest: WorkRequest) {
        workManager.enqueue(workRequest)
    }

    private val workManager: WorkManager
        get() = WorkManager.getInstance()

}
