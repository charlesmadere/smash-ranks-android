package com.garpr.android.managers

import com.garpr.android.BaseKoinTest
import com.garpr.android.BuildConfig
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.Region
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.test.inject

class AppUpgradeManagerTest : BaseKoinTest() {

    protected val appUpgradeManager: AppUpgradeManager by inject()
    protected val favoritePlayersRepository: FavoritePlayersRepository by inject()
    protected val generalPreferenceStore: GeneralPreferenceStore by inject()
    protected val identityRepository: IdentityRepository by inject()

    companion object {
        private val CHARLEZARD: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
        )

        private val IMYT: AbsPlayer = LitePlayer(
                id = "5877eb55d2994e15c7dea98b",
                name = "Imyt"
        )

        private val MIKKUZ: AbsPlayer = LitePlayer(
                id = "583a4a15d2994e0577b05c74",
                name = "mikkuz"
        )

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )
    }

    @Test
    fun testUpgradeAppFromCurrentVersion() {
        generalPreferenceStore.lastVersion.set(BuildConfig.VERSION_CODE)
        favoritePlayersRepository.addPlayer(CHARLEZARD, NORCAL)
        identityRepository.setIdentity(IMYT, NORCAL)
        appUpgradeManager.upgradeApp()

        var playersSize: Int? = null

        favoritePlayersRepository.sizeObservable.subscribe {
            playersSize = it
        }

        var identity: Optional<FavoritePlayer>? = null

        identityRepository.identityObservable.subscribe {
            identity = it
        }

        assertEquals(BuildConfig.VERSION_CODE, generalPreferenceStore.lastVersion.get())
        assertEquals(1, playersSize)
        assertEquals(IMYT, identity?.item)
    }

    @Test
    fun testUpgradeAppFromNull() {
        generalPreferenceStore.lastVersion.delete()
        favoritePlayersRepository.addPlayer(CHARLEZARD, NORCAL)
        identityRepository.setIdentity(MIKKUZ, NORCAL)
        appUpgradeManager.upgradeApp()

        var playersSize: Int? = null

        favoritePlayersRepository.sizeObservable.subscribe {
            playersSize = it
        }

        var identity: Optional<FavoritePlayer>? = null

        identityRepository.identityObservable.subscribe {
            identity = it
        }

        assertEquals(BuildConfig.VERSION_CODE, generalPreferenceStore.lastVersion.get())
        assertEquals(0, playersSize)
        assertEquals(false, identity?.isPresent)
    }

    @Test
    fun testUpgradeAppFromVersion0() {
        generalPreferenceStore.lastVersion.set(0)
        favoritePlayersRepository.addPlayer(CHARLEZARD, NORCAL)
        identityRepository.setIdentity(IMYT, NORCAL)
        appUpgradeManager.upgradeApp()

        var playersSize: Int? = null

        favoritePlayersRepository.sizeObservable.subscribe {
            playersSize = it
        }

        var identity: Optional<FavoritePlayer>? = null

        identityRepository.identityObservable.subscribe {
            identity = it
        }

        assertEquals(BuildConfig.VERSION_CODE, generalPreferenceStore.lastVersion.get())
        assertEquals(0, playersSize)
        assertEquals(false, identity?.isPresent)
    }

}
