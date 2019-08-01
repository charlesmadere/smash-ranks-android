package com.garpr.android.dagger

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkRequest
import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.TestDeviceUtilsImpl
import com.garpr.android.misc.ThreadUtils2
import com.garpr.android.wrappers.CrashlyticsWrapper
import com.garpr.android.wrappers.ImageLibraryWrapper
import com.garpr.android.wrappers.WorkManagerWrapper
import dagger.Module
import dagger.Provides
import java.util.concurrent.ExecutorService
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
    fun providesThreadUtils2(): ThreadUtils2 {
        return object : ThreadUtils2 {
            override val background: ExecutorService = Executors.newSingleThreadExecutor()
        }
    }

    @Provides
    @Singleton
    fun providesWorkManagerWrapper(): WorkManagerWrapper {
        return object : WorkManagerWrapper {
            override val configuration: Configuration
                get() = throw NotImplementedError()

            override fun cancelAllWorkByTag(tag: String) {
                // intentionally empty
            }

            override fun enqueue(workRequest: WorkRequest) {
                // intentionally empty
            }
        }
    }

}
