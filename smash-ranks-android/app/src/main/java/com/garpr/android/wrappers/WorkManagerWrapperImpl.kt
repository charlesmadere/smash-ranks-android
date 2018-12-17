package com.garpr.android.wrappers

import androidx.work.WorkManager
import androidx.work.WorkRequest

class WorkManagerWrapperImpl : WorkManagerWrapper {

    private val workManager: WorkManager
        get() = WorkManager.getInstance()

    override fun cancelAllWorkByTag(tag: String) {
        workManager.cancelAllWorkByTag(tag)
    }

    override fun enqueue(workRequest: WorkRequest) {
        workManager.enqueue(workRequest)
    }

}
