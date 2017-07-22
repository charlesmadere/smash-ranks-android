package com.garpr.android.misc

interface ThreadUtils {

    interface Task {
        fun onBackground()

        fun onUi()
    }

    fun run(task: Task)

    fun runOnBackground(task: Runnable)

    fun runOnUi(task: Runnable)

}
