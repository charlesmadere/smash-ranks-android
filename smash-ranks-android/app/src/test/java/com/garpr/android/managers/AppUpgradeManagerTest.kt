package com.garpr.android.managers

import com.garpr.android.BaseTest
import com.garpr.android.BuildConfig
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.Region
import com.garpr.android.repositories.FavoritePlayersManager
import com.garpr.android.repositories.IdentityManager
import com.garpr.android.features.splash.AppUpgradeManager
import com.garpr.android.preferences.GeneralPreferenceStore
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
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    @Inject
    protected lateinit var generalPreferenceStore: GeneralPreferenceStore

    @Inject
    protected lateinit var identityManager: IdentityManager


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
        favoritePlayersManager.addPlayer(PLAYER_1, REGION_1)
        identityManager.setIdentity(PLAYER_2, REGION_1)
        appUpgradeManager.upgradeApp()

        assertEquals(BuildConfig.VERSION_CODE, generalPreferenceStore.lastVersion.get())
        assertNull(favoritePlayersManager.absPlayers)
        assertFalse(identityManager.hasIdentity)
    }

    @Test
    fun testUpgradeAppFromCurrentVersion() {
        generalPreferenceStore.lastVersion.set(BuildConfig.VERSION_CODE)
        favoritePlayersManager.addPlayer(PLAYER_1, REGION_1)
        identityManager.setIdentity(PLAYER_2, REGION_1)
        appUpgradeManager.upgradeApp()

        assertEquals(BuildConfig.VERSION_CODE, generalPreferenceStore.lastVersion.get())
        assertEquals(1, favoritePlayersManager.absPlayers?.size ?: 0)
        assertTrue(identityManager.hasIdentity)
    }

    @Test
    fun testUpgradeAppFromNull() {
        generalPreferenceStore.lastVersion.delete()
        favoritePlayersManager.addPlayer(PLAYER_1, REGION_1)
        identityManager.setIdentity(PLAYER_2, REGION_1)
        appUpgradeManager.upgradeApp()

        assertEquals(BuildConfig.VERSION_CODE, generalPreferenceStore.lastVersion.get())
        assertNull(favoritePlayersManager.absPlayers)
        assertFalse(identityManager.hasIdentity)
    }

}
