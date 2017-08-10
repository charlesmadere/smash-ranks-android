package com.garpr.android.misc

import com.garpr.android.BaseTest
import com.garpr.android.models.AbsPlayer
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class IdentityManagerTest : BaseTest() {

    lateinit private var litePlayer: AbsPlayer
    lateinit private var rankedPlayer: AbsPlayer

    @Inject
    lateinit protected var gson: Gson

    @Inject
    lateinit protected var identityManager: IdentityManager

    @Inject
    lateinit protected var regionManager: RegionManager


    companion object {
        private const val JSON_LITE_PLAYER = "{\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"homemadewaffles\"}"
        private const val JSON_RANKED_PLAYER = "{\"rating\":41.565775187219025,\"name\":\"NMW\",\"rank\":3,\"previous_rank\":3,\"id\":\"583a4a15d2994e0577b05c8a\"}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        litePlayer = gson.fromJson(JSON_LITE_PLAYER, AbsPlayer::class.java)
        rankedPlayer = gson.fromJson(JSON_RANKED_PLAYER, AbsPlayer::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testAddListener() {
        val array = arrayOfNulls<AbsPlayer>(1)

        val listener = object : IdentityManager.OnIdentityChangeListener {
            override fun onIdentityChange(identityManager: IdentityManager) {
                array[0] = identityManager.identity
            }
        }

        identityManager.addListener(listener)
        assertNull(array[0])

        identityManager.setIdentity(litePlayer, regionManager.getRegion())
        assertEquals(litePlayer, array[0])

        identityManager.setIdentity(litePlayer, regionManager.getRegion())
        assertEquals(litePlayer, array[0])

        identityManager.setIdentity(rankedPlayer, regionManager.getRegion())
        assertEquals(rankedPlayer, array[0])

        identityManager.removeIdentity()
        assertNull(array[0])
    }

    @Test
    @Throws(Exception::class)
    fun testGetAndSetIdentity() {
        assertNull(identityManager.identity)

        identityManager.setIdentity(litePlayer, regionManager.getRegion())
        assertEquals(litePlayer, identityManager.identity)

        identityManager.setIdentity(rankedPlayer, regionManager.getRegion())
        assertEquals(rankedPlayer, identityManager.identity)

        identityManager.removeIdentity()
        assertNull(identityManager.identity)
    }

    @Test
    @Throws(Exception::class)
    fun testHasIdentity() {
        assertFalse(identityManager.hasIdentity)

        identityManager.setIdentity(litePlayer, regionManager.getRegion())
        assertTrue(identityManager.hasIdentity)

        identityManager.setIdentity(rankedPlayer, regionManager.getRegion())
        assertTrue(identityManager.hasIdentity)

        identityManager.removeIdentity()
        assertFalse(identityManager.hasIdentity)
    }

    @Test
    @Throws(Exception::class)
    fun testIsIdWithEmptyString() {
        assertFalse(identityManager.isId(""))

        identityManager.setIdentity(litePlayer, regionManager.getRegion())
        assertFalse(identityManager.isId(""))

        identityManager.removeIdentity()
        assertFalse(identityManager.isId(""))
    }

    @Test
    @Throws(Exception::class)
    fun testIsIdWithNull() {
        assertFalse(identityManager.isId(null))

        identityManager.setIdentity(litePlayer, regionManager.getRegion())
        assertFalse(identityManager.isId(null))

        identityManager.removeIdentity()
        assertFalse(identityManager.isId(null))
    }

    @Test
    @Throws(Exception::class)
    fun testIsIdWithWhitespace() {
        assertFalse(identityManager.isId(" "))

        identityManager.setIdentity(litePlayer, regionManager.getRegion())
        assertFalse(identityManager.isId("  "))

        identityManager.removeIdentity()
        assertFalse(identityManager.isId("   "))
    }

    @Test
    @Throws(Exception::class)
    fun testIsIdWithPlayer() {
        assertFalse(identityManager.isId(litePlayer.id))
        assertFalse(identityManager.isId(rankedPlayer.id))

        identityManager.setIdentity(litePlayer, regionManager.getRegion())
        assertTrue(identityManager.isId(litePlayer.id))
        assertFalse(identityManager.isId(rankedPlayer.id))

        identityManager.removeIdentity()
        assertFalse(identityManager.isId(litePlayer.id))
        assertFalse(identityManager.isId(rankedPlayer.id))
    }

    @Test
    @Throws(Exception::class)
    fun testIsPlayerWithNull() {
        assertFalse(identityManager.isPlayer(null))

        identityManager.setIdentity(rankedPlayer, regionManager.getRegion())
        assertFalse(identityManager.isPlayer(null))

        identityManager.removeIdentity()
        assertFalse(identityManager.isPlayer(null))
    }

    @Test
    @Throws(Exception::class)
    fun testIsPlayerWithPlayer() {
        assertFalse(identityManager.isPlayer(litePlayer))

        identityManager.setIdentity(rankedPlayer, regionManager.getRegion())
        assertFalse(identityManager.isPlayer(litePlayer))
        assertTrue(identityManager.isPlayer(rankedPlayer))

        identityManager.removeIdentity()
        assertFalse(identityManager.isPlayer(litePlayer))
        assertFalse(identityManager.isPlayer(rankedPlayer))
    }

    @Test
    @Throws(Exception::class)
    fun testRemoveListener() {
        val array = arrayOfNulls<AbsPlayer>(1)

        val listener = object : IdentityManager.OnIdentityChangeListener {
            override fun onIdentityChange(identityManager: IdentityManager) {
                array[0] = identityManager.identity
            }
        }

        identityManager.addListener(listener)
        identityManager.setIdentity(rankedPlayer, regionManager.getRegion())
        assertEquals(rankedPlayer, array[0])

        identityManager.removeListener(listener)
        identityManager.removeIdentity()
        assertEquals(rankedPlayer, array[0])
    }

}
