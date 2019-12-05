package com.garpr.android.koin

import com.garpr.android.misc.Constants
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.preferences.GeneralPreferenceStoreImpl
import com.garpr.android.preferences.KeyValueStore
import com.garpr.android.preferences.KeyValueStoreProvider
import com.garpr.android.preferences.KeyValueStoreProviderImpl
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import com.garpr.android.preferences.RankingsPollingPreferenceStoreImpl
import com.garpr.android.preferences.SmashRosterPreferenceStore
import com.garpr.android.preferences.SmashRosterPreferenceStoreImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val preferencesModule = module {

    single<GeneralPreferenceStore> {
        val keyValueStore: KeyValueStore = get(named(GENERAL_KEY_VALUE_STORE))
        GeneralPreferenceStoreImpl(keyValueStore, get(), Constants.DEFAULT_REGION)
    }

    single(named(FAVORITE_PLAYERS_KEY_VALUE_STORE)) {
        val context = androidContext()
        val keyValueStoreProvider: KeyValueStoreProvider = get()

        keyValueStoreProvider.getKeyValueStore(
                name = "${context.packageName}.Preferences.v2.FavoritePlayers"
        )
    }

    single(named(GENERAL_KEY_VALUE_STORE)) {
        val context = androidContext()
        val keyValueStoreProvider: KeyValueStoreProvider = get()

        keyValueStoreProvider.getKeyValueStore(
                name = "${context.packageName}.Preferences.v2.General"
        )
    }

    single(named(RANKINGS_POLLING_KEY_VALUE_STORE)) {
        val context = androidContext()
        val keyValueStoreProvider: KeyValueStoreProvider = get()

        keyValueStoreProvider.getKeyValueStore(
                name = "${context.packageName}.Preferences.v2.RankingsPolling"
        )
    }

    single(named(SMASH_ROSTER_KEY_VALUE_STORE)) {
        val context = androidContext()
        val keyValueStoreProvider: KeyValueStoreProvider = get()

        keyValueStoreProvider.getKeyValueStore(
                name = "${context.packageName}.Preferences.v2.SmashRoster"
        )
    }

    single<KeyValueStoreProvider> { KeyValueStoreProviderImpl(androidContext()) }

    single<RankingsPollingPreferenceStore> {
        val keyValueStore: KeyValueStore = get(named(RANKINGS_POLLING_KEY_VALUE_STORE))
        RankingsPollingPreferenceStoreImpl(keyValueStore, get())
    }

    single<SmashRosterPreferenceStore> {
        val keyValueStore: KeyValueStore = get(named(SMASH_ROSTER_KEY_VALUE_STORE))
        SmashRosterPreferenceStoreImpl(keyValueStore, get())
    }

}
