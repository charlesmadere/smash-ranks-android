package com.garpr.android.misc

import com.garpr.android.BaseTest
import com.garpr.android.BuildConfig
import com.garpr.android.models.AbsPlayer
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class IdentityManagerTest : BaseTest() {

    lateinit private var mPlayer: AbsPlayer

    @Inject
    lateinit protected var mGson: Gson

    @Inject
    lateinit protected var mIdentityManager: IdentityManager

    @Inject
    lateinit protected var mRegionManager: RegionManager


    companion object {
        private const val JSON_LITE_PLAYER = "{\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"homemadewaffles\"}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        mPlayer = mGson.fromJson(JSON_LITE_PLAYER, AbsPlayer::class.java)
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

        mIdentityManager.addListener(listener)
        assertNull(array[0])

        mIdentityManager.setIdentity(mPlayer, mRegionManager.getRegion())
        assertEquals(mPlayer, array[0])

        mIdentityManager.removeIdentity()
        assertNull(array[0])
    }

    @Test
    @Throws(Exception::class)
    fun testGetAndSetIdentity() {
        assertNull(mIdentityManager.identity)

        mIdentityManager.setIdentity(mPlayer, mRegionManager.getRegion())
        assertNotNull(mIdentityManager.identity)
        assertEquals(mIdentityManager.identity, mPlayer)

        mIdentityManager.removeIdentity()
        assertNull(mIdentityManager.identity)
    }

    @Test
    @Throws(Exception::class)
    fun testHasIdentity() {
        assertFalse(mIdentityManager.hasIdentity())

        mIdentityManager.setIdentity(mPlayer, mRegionManager.getRegion())
        assertTrue(mIdentityManager.hasIdentity())

        mIdentityManager.removeIdentity()
        assertFalse(mIdentityManager.hasIdentity())
    }

    @Test
    @Throws(Exception::class)
    fun testIsIdWithNull() {
        assertFalse(mIdentityManager.isId(null))

        mIdentityManager.setIdentity(mPlayer, mRegionManager.getRegion())
        assertFalse(mIdentityManager.isId(null))

        mIdentityManager.removeIdentity()
        assertFalse(mIdentityManager.isId(null))
    }

    @Test
    @Throws(Exception::class)
    fun testIsIdWithPlayer() {
        assertFalse(mIdentityManager.isId(mPlayer.id))

        mIdentityManager.setIdentity(mPlayer, mRegionManager.getRegion())
        assertTrue(mIdentityManager.isId(mPlayer.id))

        mIdentityManager.removeIdentity()
        assertFalse(mIdentityManager.isId(mPlayer.id))
    }

    @Test
    @Throws(Exception::class)
    fun testIsPlayerWithNull() {
        assertFalse(mIdentityManager.isPlayer(null))

        mIdentityManager.setIdentity(mPlayer, mRegionManager.getRegion())
        assertFalse(mIdentityManager.isPlayer(null))

        mIdentityManager.removeIdentity()
        assertFalse(mIdentityManager.isPlayer(null))
    }

    @Test
    @Throws(Exception::class)
    fun testIsPlayerWithPlayer() {
        assertFalse(mIdentityManager.isPlayer(mPlayer))

        mIdentityManager.setIdentity(mPlayer, mRegionManager.getRegion())
        assertTrue(mIdentityManager.isPlayer(mPlayer))

        mIdentityManager.removeIdentity()
        assertFalse(mIdentityManager.isPlayer(mPlayer))
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

        mIdentityManager.addListener(listener)
        mIdentityManager.setIdentity(mPlayer, mRegionManager.getRegion())
        assertEquals(mPlayer, array[0])

        mIdentityManager.removeListener(listener)
        mIdentityManager.removeIdentity()
        assertEquals(mPlayer, array[0])
    }

}
