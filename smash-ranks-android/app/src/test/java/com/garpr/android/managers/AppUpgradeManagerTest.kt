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
import org.junit.Assert.assertNull
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
        private val PLAYER_1: AbsPlayer = LitePlayer("1", "Charlezard")
        private val PLAYER_2: AbsPlayer = LitePlayer("2", "Imyt")

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )
    }

    @Test
    fun testUpgradeAppFrom0() {
        generalPreferenceStore.lastVersion.set(0)
        favoritePlayersRepository.addPlayer(PLAYER_1, NORCAL)
        identityRepository.setIdentity(PLAYER_2, NORCAL)
        appUpgradeManager.upgradeApp()

        assertEquals(BuildConfig.VERSION_CODE, generalPreferenceStore.lastVersion.get())
        assertNull(favoritePlayersRepository.players)
        assertFalse(identityRepository.hasIdentity)
    }

    @Test
    fun testUpgradeAppFromCurrentVersion() {
        generalPreferenceStore.lastVersion.set(BuildConfig.VERSION_CODE)
        favoritePlayersRepository.addPlayer(PLAYER_1, NORCAL)
        identityRepository.setIdentity(PLAYER_2, NORCAL)
        appUpgradeManager.upgradeApp()

        assertEquals(BuildConfig.VERSION_CODE, generalPreferenceStore.lastVersion.get())
        assertEquals(1, favoritePlayersRepository.players?.size ?: 0)
        assertTrue(identityRepository.hasIdentity)
    }

    @Test
    fun testUpgradeAppFromNull() {
        generalPreferenceStore.lastVersion.delete()
        favoritePlayersRepository.addPlayer(PLAYER_1, NORCAL)
        identityRepository.setIdentity(PLAYER_2, NORCAL)
        appUpgradeManager.upgradeApp()

        assertEquals(BuildConfig.VERSION_CODE, generalPreferenceStore.lastVersion.get())
        assertNull(favoritePlayersRepository.players)
        assertFalse(identityRepository.hasIdentity)
    }

}
