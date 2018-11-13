package com.garpr.android.wrappers

import androidx.work.WorkRequest

interface WorkManagerWrapper {

    fun cancelAllWorkByTag(tag: String)

    fun enqueue(workRequest: WorkRequest)

    fun initialize()

}
