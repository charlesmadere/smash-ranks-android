package com.garpr.android.features.splash

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
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class AppUpgradeManagerTest : BaseTest() {

    @Inject
    protected lateinit var appUpgradeManager: AppUpgradeManager

    @Inject
    protected lateinit var favoritePlayersRepository: FavoritePlayersRepository

    @Inject
    protected lateinit var generalPreferenceStore: GeneralPreferenceStore

    @Inject
    protected lateinit var identityRepository: IdentityRepository


    companion object {
        private val PLAYER_1: AbsPlayer = LitePlayer("1", "Charlezard")
        private val PLAYER_2: AbsPlayer = LitePlayer("2", "Imyt")

        private val REGION_1 = Region(null, null,
                null, null,
                "Norcal", "norcal", Endpoint.GAR_PR)
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    fun testUpgradeAppFrom0() {
        generalPreferenceStore.lastVersion.set(0)
        favoritePlayersRepository.addPlayer(PLAYER_1, REGION_1)
        identityRepository.setIdentity(PLAYER_2, REGION_1)
        appUpgradeManager.upgradeApp()

        assertEquals(BuildConfig.VERSION_CODE, generalPreferenceStore.lastVersion.get())
        assertNull(favoritePlayersRepository.absPlayers)
        assertFalse(identityRepository.hasIdentity)
    }

    @Test
    fun testUpgradeAppFromCurrentVersion() {
        generalPreferenceStore.lastVersion.set(BuildConfig.VERSION_CODE)
        favoritePlayersRepository.addPlayer(PLAYER_1, REGION_1)
        identityRepository.setIdentity(PLAYER_2, REGION_1)
        appUpgradeManager.upgradeApp()

        assertEquals(BuildConfig.VERSION_CODE, generalPreferenceStore.lastVersion.get())
        assertEquals(1, favoritePlayersRepository.absPlayers?.size ?: 0)
        assertTrue(identityRepository.hasIdentity)
    }

    @Test
    fun testUpgradeAppFromNull() {
        generalPreferenceStore.lastVersion.delete()
        favoritePlayersRepository.addPlayer(PLAYER_1, REGION_1)
        identityRepository.setIdentity(PLAYER_2, REGION_1)
        appUpgradeManager.upgradeApp()

        assertEquals(BuildConfig.VERSION_CODE, generalPreferenceStore.lastVersion.get())
        assertNull(favoritePlayersRepository.absPlayers)
        assertFalse(identityRepository.hasIdentity)
    }

}
