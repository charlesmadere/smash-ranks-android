package com.garpr.android.wrappers

import androidx.work.Configuration
import androidx.work.WorkRequest

interface WorkManagerWrapper {

    val configuration: Configuration

    fun cancelAllWorkByTag(tag: String)

    fun enqueue(workRequest: WorkRequest)

}
