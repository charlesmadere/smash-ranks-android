package com.garpr.android.misc

import android.os.Handler
import android.os.Looper

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ThreadUtilsImpl(
        isLowRamDevice: Boolean
) : ThreadUtils {

    private val mExecutorService: ExecutorService = Executors.newFixedThreadPool(
            if (isLowRamDevice) 2 else 3)

    private val mMainHandler: Handler = Handler(Looper.getMainLooper())


    override fun run(task: ThreadUtils.Task) {
        runOnBackground(Runnable {
            task.onBackground()

            runOnUi(Runnable {
                task.onUi()
            })
        })
    }

    override fun runOnBackground(task: Runnable) {
        mExecutorService.submit(task)
    }

    override fun runOnUi(task: Runnable) {
        mMainHandler.post(task)
    }

}
