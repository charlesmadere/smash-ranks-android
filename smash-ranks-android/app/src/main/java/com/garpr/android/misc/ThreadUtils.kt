package com.garpr.android.misc

import android.support.annotation.UiThread
import android.support.annotation.WorkerThread

interface ThreadUtils {

    fun run(task: Task)

    fun runOnBackground(task: Runnable)

    fun runOnUi(task: Runnable)

    interface Task {
        @WorkerThread
        fun onBackground()

        @UiThread
        fun onUi()
    }

}
