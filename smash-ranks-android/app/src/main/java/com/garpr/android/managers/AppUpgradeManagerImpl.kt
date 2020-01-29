package com.garpr.android.managers

import com.garpr.android.BuildConfig
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.sync.roster.SmashRosterStorage

class AppUpgradeManagerImpl(
        private val favoritePlayersRepository: FavoritePlayersRepository,
        private val generalPreferenceStore: GeneralPreferenceStore,
        private val smashRosterStorage: SmashRosterStorage,
        private val timber: Timber
) : AppUpgradeManager {

    override fun upgradeApp() {
        val lastVersion = generalPreferenceStore.lastVersion.get()
        generalPreferenceStore.lastVersion.set(BuildConfig.VERSION_CODE)

        if (lastVersion == null) {
            timber.d(TAG, "App doesn't need to upgrade as there is no previous version (is now ${BuildConfig.VERSION_CODE})")
            return
        } else if (lastVersion == BuildConfig.VERSION_CODE) {
            timber.d(TAG, "App doesn't need to upgrade, already on latest version (${BuildConfig.VERSION_CODE})")
            return
        } else {
            timber.d(TAG, "App may need to upgrade, previous version was $lastVersion (is now ${BuildConfig.VERSION_CODE})")
        }

        if (lastVersion < 1011) {
            // it used to be a String, is now a Region object
            generalPreferenceStore.currentRegion.delete()

            // it used to be an AbsPlayer, is now a FavoritePlayer
            generalPreferenceStore.identity.delete()

            // used to be a list of AbsPlayer, is now a list of FavoritePlayer
            favoritePlayersRepository.clear()
        } else if (lastVersion < 22003) {
            // migrated to Room for storage of favorite players
            favoritePlayersRepository.migrate()
        } else if (lastVersion < 22004) {
            // migrated to Room for storage of smash roster competitors
            smashRosterStorage.migrate()
        }

        timber.d(TAG, "App upgrade complete")
    }

    companion object {
        private const val TAG = "AppUpgradeManagerImpl"
    }

}
