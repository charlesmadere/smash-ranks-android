package com.garpr.android.koin

import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.DeviceUtilsImpl
import com.garpr.android.misc.StackTraceUtils
import com.garpr.android.misc.StackTraceUtilsImpl
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.ThreadUtilsImpl
import com.garpr.android.preferences.KeyValueStoreProvider
import com.garpr.android.preferences.KeyValueStoreProviderImpl
import com.garpr.android.wrappers.CrashlyticsWrapper
import com.garpr.android.wrappers.CrashlyticsWrapperImpl
import com.garpr.android.wrappers.FacebookFrescoWrapper
import com.garpr.android.wrappers.ImageLibraryWrapper
import com.garpr.android.wrappers.WorkManagerWrapper
import com.garpr.android.wrappers.WorkManagerWrapperImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val configModule = module {

    single<CrashlyticsWrapper> { CrashlyticsWrapperImpl(androidContext()) }
    single<DeviceUtils> { DeviceUtilsImpl(androidContext()) }
    single<ImageLibraryWrapper> { FacebookFrescoWrapper(androidContext(), get(), get()) }
    single<KeyValueStoreProvider> { KeyValueStoreProviderImpl(androidContext()) }
    single<StackTraceUtils> { StackTraceUtilsImpl() }
    single<String>(named(PACKAGE_NAME)) { androidContext().packageName }
    single<ThreadUtils> { ThreadUtilsImpl(get()) }
    single<WorkManagerWrapper> { WorkManagerWrapperImpl(androidContext(), get()) }

}
