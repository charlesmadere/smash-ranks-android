package com.garpr.android.misc

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ThreadUtilsImpl(
        private val deviceUtils: DeviceUtils
) : ThreadUtils {

    override val background: ExecutorService by lazy {
        Executors.newFixedThreadPool(if (deviceUtils.hasLowRam) 6 else 8)
    }

}
