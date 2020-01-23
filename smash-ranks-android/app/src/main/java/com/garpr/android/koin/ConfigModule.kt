package com.garpr.android.koin

import androidx.room.Room
import com.garpr.android.data.database.AppDatabase
import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.DeviceUtilsImpl
import com.garpr.android.misc.PackageNameProvider
import com.garpr.android.misc.PackageNameProviderImpl
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
import org.koin.dsl.module

val configModule = module {

    single {
        val threadUtils: ThreadUtils = get()
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, DATABASE_NAME)
                .setQueryExecutor(threadUtils.background)
                .setTransactionExecutor(threadUtils.background)
                .build()
    }

    single<CrashlyticsWrapper> { CrashlyticsWrapperImpl(androidContext()) }
    single<DeviceUtils> { DeviceUtilsImpl(androidContext()) }
    single<ImageLibraryWrapper> { FacebookFrescoWrapper(androidContext(), get(), get()) }
    single<KeyValueStoreProvider> { KeyValueStoreProviderImpl(androidContext()) }
    single<PackageNameProvider> { PackageNameProviderImpl(androidContext()) }
    single<StackTraceUtils> { StackTraceUtilsImpl() }
    single<ThreadUtils> { ThreadUtilsImpl(get()) }
    single<WorkManagerWrapper> { WorkManagerWrapperImpl(androidContext(), get()) }

}
