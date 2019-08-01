package com.garpr.android.dagger

import android.app.Application
import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.DeviceUtilsImpl
import com.garpr.android.misc.ThreadUtils2
import com.garpr.android.misc.ThreadUtils2Impl
import com.garpr.android.misc.Timber
import com.garpr.android.wrappers.CrashlyticsWrapper
import com.garpr.android.wrappers.CrashlyticsWrapperImpl
import com.garpr.android.wrappers.FacebookFrescoWrapper
import com.garpr.android.wrappers.ImageLibraryWrapper
import com.garpr.android.wrappers.WorkManagerWrapper
import com.garpr.android.wrappers.WorkManagerWrapperImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class ConfigModule {

    @Provides
    @Singleton
    fun providesCrashlyticsWrapper(
            application: Application
    ): CrashlyticsWrapper {
        return CrashlyticsWrapperImpl(application)
    }

    @Provides
    @Singleton
    fun providesDeviceUtils(
            application: Application
    ): DeviceUtils {
        return DeviceUtilsImpl(application)
    }

    @Provides
    @Singleton
    fun providesImageLibraryWrapper(
            application: Application,
            okHttpClient: OkHttpClient,
            timber: Timber
    ): ImageLibraryWrapper {
        return FacebookFrescoWrapper(application, okHttpClient, timber)
    }

    @Provides
    @Singleton
    fun providesThreadUtils2(
            deviceUtils: DeviceUtils
    ): ThreadUtils2 {
        return ThreadUtils2Impl(deviceUtils)
    }

    @Provides
    @Singleton
    fun providesWorkManagerWrapper(
            application: Application,
            threadUtils: ThreadUtils2
    ): WorkManagerWrapper {
        return WorkManagerWrapperImpl(application, threadUtils)
    }

}
