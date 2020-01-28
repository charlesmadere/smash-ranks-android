package com.garpr.android.koin

import com.garpr.android.misc.PackageNameProvider
import com.garpr.android.sync.rankings.RankingsNotificationsUtils
import com.garpr.android.sync.rankings.RankingsNotificationsUtilsImpl
import com.garpr.android.sync.rankings.RankingsPollingManager
import com.garpr.android.sync.rankings.RankingsPollingManagerImpl
import com.garpr.android.sync.roster.SmashRosterStorage
import com.garpr.android.sync.roster.SmashRosterStorageImpl
import com.garpr.android.sync.roster.SmashRosterSyncManager
import com.garpr.android.sync.roster.SmashRosterSyncManagerImpl
import org.koin.dsl.module

val syncModule = module {

    single<RankingsNotificationsUtils> { RankingsNotificationsUtilsImpl(get(), get()) }
    single<RankingsPollingManager> { RankingsPollingManagerImpl(get(), get(), get()) }

    single<SmashRosterStorage> {
        val packageNameProvider: PackageNameProvider = get()
        SmashRosterStorageImpl(get(), get(), get(), packageNameProvider.packageName, get(), get())
    }

    single<SmashRosterSyncManager> { SmashRosterSyncManagerImpl(get(), get(), get(), get(), get(), get()) }

}
