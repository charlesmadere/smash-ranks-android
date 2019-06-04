package com.garpr.android.managers

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.repositories.IdentityManager
import com.garpr.android.repositories.RegionManager
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
class IdentityManagerTest : BaseTest() {

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var regionManager: RegionManager


    companion object {
        private val LITE_PLAYER = LitePlayer(
                id = "583a4a15d2994e0577b05c74",
                name = "homemadewaffles"
        )

        private val RANKED_PLAYER = RankedPlayer(
                id = "583a4a15d2994e0577b05c8a",
                name = "NMW",
                rating = 41.565775187219025f,
                rank = 3,
                previousRank = 3
        )
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    fun testAddListener() {
        var identity: AbsPlayer? = null

        val listener = object : IdentityManager.OnIdentityChangeListener {
            override fun onIdentityChange(identityManager: IdentityManager) {
                identity = identityManager.identity
            }
        }

        identityManager.addListener(listener)
        assertNull(identity)

        identityManager.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertEquals(LITE_PLAYER, identity)

        identityManager.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertEquals(LITE_PLAYER, identity)

        identityManager.setIdentity(RANKED_PLAYER, regionManager.getRegion())
        assertEquals(RANKED_PLAYER, identity)

        identityManager.removeIdentity()
        assertNull(identity)
    }

    @Test
    fun testAddListenerTwice() {
        var count = 0

        val listener = object : IdentityManager.OnIdentityChangeListener {
            override fun onIdentityChange(identityManager: IdentityManager) {
                ++count
            }
        }

        identityManager.addListener(listener)
        identityManager.addListener(listener)
        identityManager.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertEquals(1, count)
    }

    @Test
    fun testGetIdentity() {
        assertNull(identityManager.identity)
    }

    @Test
    fun testGetAndSetIdentity() {
        assertNull(identityManager.identity)

        identityManager.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertEquals(LITE_PLAYER, identityManager.identity)

        identityManager.setIdentity(RANKED_PLAYER, regionManager.getRegion())
        assertEquals(RANKED_PLAYER, identityManager.identity)

        identityManager.removeIdentity()
        assertNull(identityManager.identity)
    }

    @Test
    fun testHasIdentity() {
        assertFalse(identityManager.hasIdentity)

        identityManager.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertTrue(identityManager.hasIdentity)

        identityManager.setIdentity(RANKED_PLAYER, regionManager.getRegion())
        assertTrue(identityManager.hasIdentity)

        identityManager.removeIdentity()
        assertFalse(identityManager.hasIdentity)
    }

    @Test
    fun testIsPlayerWithEmptyString() {
        assertFalse(identityManager.isPlayer(""))

        identityManager.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertFalse(identityManager.isPlayer(""))

        identityManager.removeIdentity()
        assertFalse(identityManager.isPlayer(""))
    }

    @Test
    fun testIsPlayerWithIdString() {
        assertFalse(identityManager.isPlayer(LITE_PLAYER.id))
        assertFalse(identityManager.isPlayer(RANKED_PLAYER.id))

        identityManager.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertTrue(identityManager.isPlayer(LITE_PLAYER.id))
        assertFalse(identityManager.isPlayer(RANKED_PLAYER.id))

        identityManager.removeIdentity()
        assertFalse(identityManager.isPlayer(LITE_PLAYER.id))
        assertFalse(identityManager.isPlayer(RANKED_PLAYER.id))
    }

    @Test
    fun testIsPlayerWithNullPlayer() {
        assertFalse(identityManager.isPlayer(null as AbsPlayer?))

        identityManager.setIdentity(RANKED_PLAYER, regionManager.getRegion())
        assertFalse(identityManager.isPlayer(null as AbsPlayer?))

        identityManager.removeIdentity()
        assertFalse(identityManager.isPlayer(null as AbsPlayer?))
    }

    @Test
    fun testIsPlayerWithNullString() {
        assertFalse(identityManager.isPlayer(null as String?))

        identityManager.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertFalse(identityManager.isPlayer(null as String?))

        identityManager.removeIdentity()
        assertFalse(identityManager.isPlayer(null as String?))
    }

    @Test
    fun testIsPlayerWithPlayer() {
        assertFalse(identityManager.isPlayer(LITE_PLAYER))

        identityManager.setIdentity(RANKED_PLAYER, regionManager.getRegion())
        assertFalse(identityManager.isPlayer(LITE_PLAYER))
        assertTrue(identityManager.isPlayer(RANKED_PLAYER))

        identityManager.removeIdentity()
        assertFalse(identityManager.isPlayer(LITE_PLAYER))
        assertFalse(identityManager.isPlayer(RANKED_PLAYER))
    }

    @Test
    fun testIsPlayerWithWhitespaceString() {
        assertFalse(identityManager.isPlayer(" "))

        identityManager.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertFalse(identityManager.isPlayer("  "))

        identityManager.removeIdentity()
        assertFalse(identityManager.isPlayer("   "))
    }

    @Test
    fun testRemoveListener() {
        var identity: AbsPlayer? = null

        val listener = object : IdentityManager.OnIdentityChangeListener {
            override fun onIdentityChange(identityManager: IdentityManager) {
                identity = identityManager.identity
            }
        }

        identityManager.addListener(listener)
        identityManager.setIdentity(RANKED_PLAYER, regionManager.getRegion())
        assertEquals(RANKED_PLAYER, identity)

        identityManager.removeListener(listener)
        identityManager.removeIdentity()
        assertEquals(RANKED_PLAYER, identity)
    }

}
