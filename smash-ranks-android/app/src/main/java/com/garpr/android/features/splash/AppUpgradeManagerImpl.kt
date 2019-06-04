package com.garpr.android.features.splash

import com.garpr.android.BuildConfig
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.repositories.FavoritePlayersRepository

class AppUpgradeManagerImpl(
        private val favoritePlayersRepository: FavoritePlayersRepository,
        private val generalPreferenceStore: GeneralPreferenceStore,
        private val timber: Timber
) : AppUpgradeManager {

    companion object {
        private const val TAG = "AppUpgradeManagerImpl"
    }

    override fun upgradeApp() {
        val lastVersionPref = generalPreferenceStore.lastVersion
        val lastVersion = lastVersionPref.get()

        if (lastVersion == null) {
            timber.d(TAG, "App has no previous version, is now ${BuildConfig.VERSION_CODE}")
        } else if (lastVersion < BuildConfig.VERSION_CODE) {
            timber.d(TAG, "App's previous version was $lastVersion, is now ${BuildConfig.VERSION_CODE}")
        } else {
            timber.d(TAG, "App doesn't need to upgrade (on version $lastVersion)")
            return
        }

        lastVersionPref.set(BuildConfig.VERSION_CODE)

        if (lastVersion == null || lastVersion < 1011) {
            // it used to be a String, is now a Region
            generalPreferenceStore.currentRegion.delete()

            // it used to be an AbsPlayer, is now a FavoritePlayer
            generalPreferenceStore.identity.delete()

            // used to be a list of AbsPlayer, is now a list of FavoritePlayer
            favoritePlayersRepository.clear()
        }
    }

}
