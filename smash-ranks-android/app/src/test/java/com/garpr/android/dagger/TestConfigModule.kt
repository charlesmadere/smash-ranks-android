package com.garpr.android.dagger

import android.app.Application
import androidx.work.WorkRequest
import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.TestDeviceUtilsImpl
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.wrappers.CrashlyticsWrapper
import com.garpr.android.wrappers.ImageLibraryWrapper
import com.garpr.android.wrappers.WorkManagerWrapper
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class TestConfigModule {

    @Provides
    @Singleton
    fun providesCrashlyticsWrapper(): CrashlyticsWrapper {
        return object : CrashlyticsWrapper {
            override fun initialize(disabled: Boolean) {
                // intentionally empty
            }

            override fun log(priority: Int, tag: String, msg: String) {
                // intentionally empty
            }

            override fun logException(tr: Throwable) {
                // intentionally empty
            }

            override fun setBool(key: String, value: Boolean) {
                // intentionally empty
            }

            override fun setInt(key: String, value: Int) {
                // intentionally empty
            }

            override fun setString(key: String, value: String) {
                // intentionally empty
            }
        }
    }

    @Provides
    @Singleton
    fun providesDeviceUtils(
            application: Application
    ): DeviceUtils {
        return TestDeviceUtilsImpl(application)
    }

    @Provides
    @Singleton
    fun providesImageLibraryWrapper(): ImageLibraryWrapper {
        return object : ImageLibraryWrapper {
            override fun initialize() {
                // intentionally empty
            }
        }
    }

    @Provides
    @Singleton
    fun providesThreadUtils(): ThreadUtils {
        return object : ThreadUtils {
            override val executorService = Executors.newSingleThreadExecutor()

            override val isUiThread: Boolean
                get() = false

            override fun run(task: ThreadUtils.Task) {
                task.onBackground()
                task.onUi()
            }

            override fun runOnBackground(task: Runnable) {
                task.run()
            }

            override fun runOnUi(task: Runnable) {
                task.run()
            }
        }
    }

    @Provides
    @Singleton
    fun providesWorkManagerWrapper(): WorkManagerWrapper {
        return object : WorkManagerWrapper {
            override fun cancelAllWorkByTag(tag: String) {
                // intentionally empty
            }

            override fun enqueue(workRequest: WorkRequest) {
                // intentionally empty
            }
        }
    }

}
