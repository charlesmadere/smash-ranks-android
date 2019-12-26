package com.garpr.android.managers

import com.garpr.android.BaseTest
import com.garpr.android.BuildConfig
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.Region
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AppUpgradeManagerTest : BaseTest() {

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

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )
    }

    @Test
    fun testUpgradeAppFrom0() {
        generalPreferenceStore.lastVersion.set(0)
        favoritePlayersRepository.addPlayer(CHARLEZARD, NORCAL)
        identityRepository.setIdentity(IMYT, NORCAL)
        appUpgradeManager.upgradeApp()

        assertEquals(BuildConfig.VERSION_CODE, generalPreferenceStore.lastVersion.get())
        assertTrue(favoritePlayersRepository.players.isNullOrEmpty())
        assertFalse(identityRepository.hasIdentity)
    }

    @Test
    fun testUpgradeAppFromCurrentVersion() {
        generalPreferenceStore.lastVersion.set(BuildConfig.VERSION_CODE)
        favoritePlayersRepository.addPlayer(CHARLEZARD, NORCAL)
        identityRepository.setIdentity(IMYT, NORCAL)
        appUpgradeManager.upgradeApp()

        assertEquals(BuildConfig.VERSION_CODE, generalPreferenceStore.lastVersion.get())
        assertEquals(1, favoritePlayersRepository.players?.size ?: 0)
        assertTrue(identityRepository.hasIdentity)
    }

    @Test
    fun testUpgradeAppFromNull() {
        generalPreferenceStore.lastVersion.delete()
        favoritePlayersRepository.addPlayer(CHARLEZARD, NORCAL)
        identityRepository.setIdentity(IMYT, NORCAL)
        appUpgradeManager.upgradeApp()

        assertEquals(BuildConfig.VERSION_CODE, generalPreferenceStore.lastVersion.get())
        assertTrue(favoritePlayersRepository.players.isNullOrEmpty())
        assertFalse(identityRepository.hasIdentity)
    }

}
