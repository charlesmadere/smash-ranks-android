package com.garpr.android.koin

import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.DeviceUtilsImpl
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.ThreadUtilsImpl
import com.garpr.android.wrappers.CrashlyticsWrapper
import com.garpr.android.wrappers.CrashlyticsWrapperImpl
import com.garpr.android.wrappers.FacebookFrescoWrapper
import com.garpr.android.wrappers.ImageLibraryWrapper
import com.garpr.android.wrappers.WorkManagerWrapper
import com.garpr.android.wrappers.WorkManagerWrapperImpl
import org.koin.dsl.module

val configModule = module {

    single<CrashlyticsWrapper> { CrashlyticsWrapperImpl(get()) }
    single<DeviceUtils> { DeviceUtilsImpl(get()) }
    single<ImageLibraryWrapper> { FacebookFrescoWrapper(get(), get(), get()) }
    single<ThreadUtils> { ThreadUtilsImpl(get()) }
    single<WorkManagerWrapper> { WorkManagerWrapperImpl(get(), get()) }

}
