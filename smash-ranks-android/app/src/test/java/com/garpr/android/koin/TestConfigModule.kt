package com.garpr.android.koin

import androidx.work.Configuration
import androidx.work.WorkRequest
import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.PackageNameProvider
import com.garpr.android.misc.StackTraceUtils
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.preferences.KeyValueStoreProvider
import com.garpr.android.preferences.TestKeyValueStoreProviderImpl
import com.garpr.android.test.TestDeviceUtilsImpl
import com.garpr.android.test.TestThreadUtilsImpl
import com.garpr.android.wrappers.CrashlyticsWrapper
import com.garpr.android.wrappers.ImageLibraryWrapper
import com.garpr.android.wrappers.WorkManagerWrapper
import org.koin.dsl.module

val testConfigModule = module {

    single<CrashlyticsWrapper> {
        object : CrashlyticsWrapper {
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

    single<DeviceUtils> { TestDeviceUtilsImpl() }

    single<ImageLibraryWrapper> {
        object : ImageLibraryWrapper {
            override fun initialize() {
                // intentionally empty
            }
        }
    }

    single<KeyValueStoreProvider> { TestKeyValueStoreProviderImpl() }

    single<PackageNameProvider> {
        object : PackageNameProvider {
            override val packageName: String = "com.garpr.android.test"
        }
    }

    single<StackTraceUtils> {
        object : StackTraceUtils {
            override fun toString(throwable: Throwable?): String {
                return throwable?.javaClass?.simpleName ?: ""
            }
        }
    }

    single<ThreadUtils> { TestThreadUtilsImpl() }

    single<WorkManagerWrapper> {
        object : WorkManagerWrapper {
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
