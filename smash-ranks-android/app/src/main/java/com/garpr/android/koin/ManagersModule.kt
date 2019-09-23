package com.garpr.android.koin

import com.garpr.android.features.notifications.NotificationsManager
import com.garpr.android.features.notifications.NotificationsManagerImpl
import com.garpr.android.features.player.PlayerProfileManager
import com.garpr.android.features.player.PlayerProfileManagerImpl
import com.garpr.android.managers.AppUpgradeManager
import com.garpr.android.managers.AppUpgradeManagerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val managersModule = module {

    single<AppUpgradeManager> { AppUpgradeManagerImpl(get(), get(), get()) }
    single<NotificationsManager> { NotificationsManagerImpl(androidContext(), get(), get(), get()) }
    single<PlayerProfileManager> { PlayerProfileManagerImpl(androidContext(), get(), get()) }

}
