package com.garpr.android.managers

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.repositories.IdentityRepository
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
class IdentityRepositoryTest : BaseTest() {

    @Inject
    protected lateinit var identityRepository: IdentityRepository

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

        val listener = object : IdentityRepository.OnIdentityChangeListener {
            override fun onIdentityChange(identityRepository: IdentityRepository) {
                identity = identityRepository.identity
            }
        }

        identityRepository.addListener(listener)
        assertNull(identity)

        identityRepository.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertEquals(LITE_PLAYER, identity)

        identityRepository.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertEquals(LITE_PLAYER, identity)

        identityRepository.setIdentity(RANKED_PLAYER, regionManager.getRegion())
        assertEquals(RANKED_PLAYER, identity)

        identityRepository.removeIdentity()
        assertNull(identity)
    }

    @Test
    fun testAddListenerTwice() {
        var count = 0

        val listener = object : IdentityRepository.OnIdentityChangeListener {
            override fun onIdentityChange(identityRepository: IdentityRepository) {
                ++count
            }
        }

        identityRepository.addListener(listener)
        identityRepository.addListener(listener)
        identityRepository.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertEquals(1, count)
    }

    @Test
    fun testGetIdentity() {
        assertNull(identityRepository.identity)
    }

    @Test
    fun testGetAndSetIdentity() {
        assertNull(identityRepository.identity)

        identityRepository.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertEquals(LITE_PLAYER, identityRepository.identity)

        identityRepository.setIdentity(RANKED_PLAYER, regionManager.getRegion())
        assertEquals(RANKED_PLAYER, identityRepository.identity)

        identityRepository.removeIdentity()
        assertNull(identityRepository.identity)
    }

    @Test
    fun testHasIdentity() {
        assertFalse(identityRepository.hasIdentity)

        identityRepository.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertTrue(identityRepository.hasIdentity)

        identityRepository.setIdentity(RANKED_PLAYER, regionManager.getRegion())
        assertTrue(identityRepository.hasIdentity)

        identityRepository.removeIdentity()
        assertFalse(identityRepository.hasIdentity)
    }

    @Test
    fun testIsPlayerWithEmptyString() {
        assertFalse(identityRepository.isPlayer(""))

        identityRepository.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertFalse(identityRepository.isPlayer(""))

        identityRepository.removeIdentity()
        assertFalse(identityRepository.isPlayer(""))
    }

    @Test
    fun testIsPlayerWithIdString() {
        assertFalse(identityRepository.isPlayer(LITE_PLAYER.id))
        assertFalse(identityRepository.isPlayer(RANKED_PLAYER.id))

        identityRepository.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertTrue(identityRepository.isPlayer(LITE_PLAYER.id))
        assertFalse(identityRepository.isPlayer(RANKED_PLAYER.id))

        identityRepository.removeIdentity()
        assertFalse(identityRepository.isPlayer(LITE_PLAYER.id))
        assertFalse(identityRepository.isPlayer(RANKED_PLAYER.id))
    }

    @Test
    fun testIsPlayerWithNullPlayer() {
        assertFalse(identityRepository.isPlayer(null as AbsPlayer?))

        identityRepository.setIdentity(RANKED_PLAYER, regionManager.getRegion())
        assertFalse(identityRepository.isPlayer(null as AbsPlayer?))

        identityRepository.removeIdentity()
        assertFalse(identityRepository.isPlayer(null as AbsPlayer?))
    }

    @Test
    fun testIsPlayerWithNullString() {
        assertFalse(identityRepository.isPlayer(null as String?))

        identityRepository.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertFalse(identityRepository.isPlayer(null as String?))

        identityRepository.removeIdentity()
        assertFalse(identityRepository.isPlayer(null as String?))
    }

    @Test
    fun testIsPlayerWithPlayer() {
        assertFalse(identityRepository.isPlayer(LITE_PLAYER))

        identityRepository.setIdentity(RANKED_PLAYER, regionManager.getRegion())
        assertFalse(identityRepository.isPlayer(LITE_PLAYER))
        assertTrue(identityRepository.isPlayer(RANKED_PLAYER))

        identityRepository.removeIdentity()
        assertFalse(identityRepository.isPlayer(LITE_PLAYER))
        assertFalse(identityRepository.isPlayer(RANKED_PLAYER))
    }

    @Test
    fun testIsPlayerWithWhitespaceString() {
        assertFalse(identityRepository.isPlayer(" "))

        identityRepository.setIdentity(LITE_PLAYER, regionManager.getRegion())
        assertFalse(identityRepository.isPlayer("  "))

        identityRepository.removeIdentity()
        assertFalse(identityRepository.isPlayer("   "))
    }

    @Test
    fun testRemoveListener() {
        var identity: AbsPlayer? = null

        val listener = object : IdentityRepository.OnIdentityChangeListener {
            override fun onIdentityChange(identityRepository: IdentityRepository) {
                identity = identityRepository.identity
            }
        }

        identityRepository.addListener(listener)
        identityRepository.setIdentity(RANKED_PLAYER, regionManager.getRegion())
        assertEquals(RANKED_PLAYER, identity)

        identityRepository.removeListener(listener)
        identityRepository.removeIdentity()
        assertEquals(RANKED_PLAYER, identity)
    }

}
