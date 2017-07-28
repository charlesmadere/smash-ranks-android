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
class FavoritePlayersManagerTest : BaseTest() {

    lateinit private var mPlayer1: AbsPlayer
    lateinit private var mPlayer2: AbsPlayer

    @Inject
    lateinit protected var mFavoritePlayersManager: FavoritePlayersManager

    @Inject
    lateinit protected var mGson: Gson

    @Inject
    lateinit protected var mRegionManager: RegionManager


    companion object {
        private const val JSON_PLAYER_1 = "{\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"homemadewaffles\"}"
        private const val JSON_PLAYER_2 = "{\"id\":\"5877eb55d2994e15c7dea97e\",\"name\":\"Spark\"}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        mPlayer1 = mGson.fromJson(JSON_PLAYER_1, AbsPlayer::class.java)
        mPlayer2 = mGson.fromJson(JSON_PLAYER_2, AbsPlayer::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testAbsPlayers() {
        var absPlayers = mFavoritePlayersManager.absPlayers
        assertTrue(absPlayers == null || absPlayers.isEmpty())

        mFavoritePlayersManager.addPlayer(mPlayer2, mRegionManager.region)
        mFavoritePlayersManager.addPlayer(mPlayer1, mRegionManager.region)

        absPlayers = mFavoritePlayersManager.absPlayers
        assertNotNull(absPlayers)
        assertEquals(2, absPlayers?.size)

        assertEquals(mPlayer1, absPlayers?.get(0))
        assertEquals(mPlayer2, absPlayers?.get(1))
    }

    @Test
    @Throws(Exception::class)
    fun testAddListener() {
        val array = arrayOfNulls<List<*>>(1)

        val listener = object : FavoritePlayersManager.OnFavoritePlayersChangeListener {
            override fun onFavoritePlayersChanged(manager: FavoritePlayersManager) {
                array[0] = manager.players
            }
        }

        mFavoritePlayersManager.addListener(listener)
        assertNull(array[0])

        mFavoritePlayersManager.addPlayer(mPlayer1, mRegionManager.region)
        assertNotNull(array[0])
        assertEquals(1, array[0]?.size)
        assertEquals(mPlayer1, array[0]?.get(0))

        mFavoritePlayersManager.removePlayer(mPlayer1)
        assertTrue(array[0] == null || array[0]?.isEmpty() == true)
    }

    @Test
    @Throws(Exception::class)
    fun testAddPlayer() {
        mFavoritePlayersManager.addPlayer(mPlayer1, mRegionManager.region)

        val players = mFavoritePlayersManager.players
        assertNotNull(players)
        assertEquals(1, players?.size)
        assertEquals(mPlayer1, players?.get(0))
    }

    @Test
    @Throws(Exception::class)
    fun testAddPlayers() {
        mFavoritePlayersManager.addPlayer(mPlayer1, mRegionManager.region)
        mFavoritePlayersManager.addPlayer(mPlayer2, mRegionManager.region)

        val players = mFavoritePlayersManager.players
        assertNotNull(players)
        assertEquals(2, players?.size)
        assertEquals(mPlayer1, players?.get(0))
        assertEquals(mPlayer2, players?.get(1))
    }

    @Test
    @Throws(Exception::class)
    fun testClear() {
        mFavoritePlayersManager.clear()
        assertTrue(mFavoritePlayersManager.isEmpty)

        mFavoritePlayersManager.addPlayer(mPlayer1, mRegionManager.region)
        assertFalse(mFavoritePlayersManager.isEmpty)

        mFavoritePlayersManager.clear()
        assertTrue(mFavoritePlayersManager.isEmpty)

        mFavoritePlayersManager.addPlayer(mPlayer2, mRegionManager.region)
        mFavoritePlayersManager.addPlayer(mPlayer1, mRegionManager.region)
        mFavoritePlayersManager.clear()
        assertTrue(mFavoritePlayersManager.isEmpty)

    }

    @Test
    @Throws(Exception::class)
    fun testContains() {
        assertFalse(mPlayer1 in mFavoritePlayersManager)
        assertFalse(mPlayer1.id in mFavoritePlayersManager)
        assertFalse(mPlayer2 in mFavoritePlayersManager)
        assertFalse(mPlayer2.id in mFavoritePlayersManager)

        mFavoritePlayersManager.addPlayer(mPlayer1, mRegionManager.region)
        assertTrue(mPlayer1 in mFavoritePlayersManager)
        assertTrue(mPlayer1.id in mFavoritePlayersManager)
        assertFalse(mPlayer2 in mFavoritePlayersManager)
        assertFalse(mPlayer2.id in mFavoritePlayersManager)

        mFavoritePlayersManager.removePlayer(mPlayer2)
        assertTrue(mPlayer1 in mFavoritePlayersManager)
        assertTrue(mPlayer1.id in mFavoritePlayersManager)
        assertFalse(mPlayer2 in mFavoritePlayersManager)
        assertFalse(mPlayer2.id in mFavoritePlayersManager)

        mFavoritePlayersManager.removePlayer(mPlayer1)
        assertFalse(mPlayer1 in mFavoritePlayersManager)
        assertFalse(mPlayer1.id in mFavoritePlayersManager)
        assertFalse(mPlayer2 in mFavoritePlayersManager)
        assertFalse(mPlayer2.id in mFavoritePlayersManager)

        mFavoritePlayersManager.addPlayer(mPlayer2, mRegionManager.region)
        assertFalse(mPlayer1 in mFavoritePlayersManager)
        assertFalse(mPlayer1.id in mFavoritePlayersManager)
        assertTrue(mPlayer2 in mFavoritePlayersManager)
        assertTrue(mPlayer2.id in mFavoritePlayersManager)

        mFavoritePlayersManager.addPlayer(mPlayer1, mRegionManager.region)
        assertTrue(mPlayer1 in mFavoritePlayersManager)
        assertTrue(mPlayer1.id in mFavoritePlayersManager)
        assertTrue(mPlayer2 in mFavoritePlayersManager)
        assertTrue(mPlayer2.id in mFavoritePlayersManager)

        mFavoritePlayersManager.clear()
        assertFalse(mPlayer1 in mFavoritePlayersManager)
        assertFalse(mPlayer1.id in mFavoritePlayersManager)
        assertFalse(mPlayer2 in mFavoritePlayersManager)
        assertFalse(mPlayer2.id in mFavoritePlayersManager)
    }

    @Test
    @Throws(Exception::class)
    fun testIsEmpty() {
        assertTrue(mFavoritePlayersManager.isEmpty)

        mFavoritePlayersManager.addPlayer(mPlayer1, mRegionManager.region)
        assertFalse(mFavoritePlayersManager.isEmpty)

        mFavoritePlayersManager.removePlayer(mPlayer2)
        assertFalse(mFavoritePlayersManager.isEmpty)

        mFavoritePlayersManager.removePlayer(mPlayer2.id)
        assertFalse(mFavoritePlayersManager.isEmpty)

        mFavoritePlayersManager.removePlayer(mPlayer1.id)
        assertTrue(mFavoritePlayersManager.isEmpty)
    }

    @Test
    @Throws(Exception::class)
    fun testPlayers() {
        var players = mFavoritePlayersManager.players
        assertTrue(players == null || players.isEmpty())

        var absPlayers = mFavoritePlayersManager.absPlayers
        assertTrue(absPlayers == null || absPlayers.isEmpty())

        mFavoritePlayersManager.addPlayer(mPlayer2, mRegionManager.region)
        players = mFavoritePlayersManager.players
        assertNotNull(players)
        assertEquals(1, players?.size)

        absPlayers = mFavoritePlayersManager.absPlayers
        assertNotNull(absPlayers)
        assertEquals(1, absPlayers?.size)

        mFavoritePlayersManager.addPlayer(mPlayer1, mRegionManager.region)
        players = mFavoritePlayersManager.players
        assertNotNull(players)
        assertEquals(2, players?.size)

        absPlayers = mFavoritePlayersManager.absPlayers
        assertNotNull(absPlayers)
        assertEquals(2, absPlayers?.size)

        mFavoritePlayersManager.addPlayer(mPlayer2, mRegionManager.region)
        players = mFavoritePlayersManager.players
        assertNotNull(players)
        assertEquals(1, players?.size)

        absPlayers = mFavoritePlayersManager.absPlayers
        assertNotNull(absPlayers)
        assertEquals(1, absPlayers?.size)

        mFavoritePlayersManager.removePlayer(mPlayer2)
        players = mFavoritePlayersManager.players
        assertNull(players)

        absPlayers = mFavoritePlayersManager.absPlayers
        assertNull(absPlayers)
    }

    @Test
    @Throws(Exception::class)
    fun testRemoveListener() {
        val array = arrayOfNulls<List<*>>(1)

        val listener = object : FavoritePlayersManager.OnFavoritePlayersChangeListener {
            override fun onFavoritePlayersChanged(manager: FavoritePlayersManager) {
                array[0] = manager.players
            }
        }

        mFavoritePlayersManager.addListener(listener)
        assertNull(array[0])

        mFavoritePlayersManager.addPlayer(mPlayer1, mRegionManager.region)
        assertNotNull(array[0])
        assertEquals(1, array[0]?.size)
        assertEquals(mPlayer1, array[0])

        mFavoritePlayersManager.removeListener(listener)
        mFavoritePlayersManager.removePlayer(mPlayer1)
        assertNotNull(array[0])
        assertEquals(1, array[0]?.size)
        assertEquals(mPlayer1, array[0])
    }

    @Test
    @Throws(Exception::class)
    fun testRemovePlayers() {
        mFavoritePlayersManager.addPlayer(mPlayer1, mRegionManager.region)
        mFavoritePlayersManager.addPlayer(mPlayer2, mRegionManager.region)

        mFavoritePlayersManager.removePlayer(mPlayer1)
        mFavoritePlayersManager.removePlayer(mPlayer2)
        var players = mFavoritePlayersManager.players
        assertTrue(players == null || players.isEmpty())
        assertTrue(mFavoritePlayersManager.isEmpty)

        mFavoritePlayersManager.addPlayer(mPlayer2, mRegionManager.region)
        mFavoritePlayersManager.addPlayer(mPlayer1, mRegionManager.region)
        mFavoritePlayersManager.removePlayer(mPlayer2.id)
        mFavoritePlayersManager.removePlayer(mPlayer1.id)
        players = mFavoritePlayersManager.players
        assertTrue(players == null || players.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testSize() {
        assertEquals(0, mFavoritePlayersManager.size)

        mFavoritePlayersManager.addPlayer(mPlayer1, mRegionManager.region)
        assertEquals(1, mFavoritePlayersManager.size)

        mFavoritePlayersManager.addPlayer(mPlayer2, mRegionManager.region)
        assertEquals(2, mFavoritePlayersManager.size)

        mFavoritePlayersManager.addPlayer(mPlayer1, mRegionManager.region)
        assertEquals(2, mFavoritePlayersManager.size)

        mFavoritePlayersManager.removePlayer(mPlayer1)
        assertEquals(1, mFavoritePlayersManager.size)

        mFavoritePlayersManager.removePlayer(mPlayer1)
        assertEquals(1, mFavoritePlayersManager.size)

        mFavoritePlayersManager.clear()
        assertEquals(0, mFavoritePlayersManager.size)

        mFavoritePlayersManager.removePlayer(mPlayer2)
        assertEquals(0, mFavoritePlayersManager.size)
    }

}
