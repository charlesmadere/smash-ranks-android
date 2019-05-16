package com.garpr.android.misc

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import java.util.concurrent.ExecutorService

interface ThreadUtils {

    val executorService: ExecutorService

    val isUiThread: Boolean

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
