package com.garpr.android.misc

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.PlayersBundle
import com.garpr.android.data.models.Region
import com.garpr.android.misc.PlayerListBuilder.PlayerListItem
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
class PlayerListBuilderTest : BaseTest() {

    protected val identityRepository: IdentityRepository by inject()
    protected val playerListBuilder: PlayerListBuilder by inject()

    companion object {
        private val CHARLEZARD: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
        )

        private val IMYT: AbsPlayer = LitePlayer(
                id = "5877eb55d2994e15c7dea98b",
                name = "Imyt"
        )

        private val SFAT: AbsPlayer = LitePlayer(
                id = "588852e8d2994e3bbfa52d88",
                name = "SFAT"
        )

        private val SNAP: AbsPlayer = LitePlayer(
                id = "59213f1ad2994e1d79144956",
                name = "Snap"
        )

        private val SIX_LASERS: AbsPlayer = LitePlayer(
                id = "588999c4d2994e713ad638fa",
                name = "6 lasers"
        )

        private val KIM: AbsPlayer = LitePlayer(
                id = "588999c5d2994e713ad63c9b",
                name = "\$\$\$ | Kim\$\$\$"
        )

        private val EMPTY_PLAYERS_BUNDLE = PlayersBundle()

        private val PLAYERS_BUNDLE = PlayersBundle(
                players = listOf(CHARLEZARD, IMYT, SFAT, SNAP, SIX_LASERS, KIM)
        )

        private val NORCAL = Region(
                id = "norcal",
                displayName = "Norcal",
                endpoint = Endpoint.GAR_PR
        )
    }

    @Test
    fun testCreate() {
        val list = playerListBuilder.create(PLAYERS_BUNDLE)
        assertEquals(11, list?.size)

        var divider = list?.get(0) as PlayerListItem.Divider
        assertEquals("C", (divider as PlayerListItem.Divider.Letter).letter)

        var player = list[1] as PlayerListItem.Player
        assertEquals(CHARLEZARD, player.player)
        assertFalse(player.isIdentity)

        divider = list[2] as PlayerListItem.Divider
        assertEquals("I", (divider as PlayerListItem.Divider.Letter).letter)

        player = list[3] as PlayerListItem.Player
        assertEquals(IMYT, player.player)
        assertFalse(player.isIdentity)

        divider = list[4] as PlayerListItem.Divider
        assertEquals("S", (divider as PlayerListItem.Divider.Letter).letter)

        player = list[5] as PlayerListItem.Player
        assertEquals(SFAT, player.player)
        assertFalse(player.isIdentity)

        player = list[6] as PlayerListItem.Player
        assertEquals(SNAP, player.player)
        assertFalse(player.isIdentity)

        divider = list[7] as PlayerListItem.Divider
        assertTrue(divider is PlayerListItem.Divider.Digit)

        player = list[8] as PlayerListItem.Player
        assertEquals(SIX_LASERS, player.player)
        assertFalse(player.isIdentity)

        divider = list[9] as PlayerListItem.Divider
        assertTrue(divider is PlayerListItem.Divider.Other)

        player = list[10] as PlayerListItem.Player
        assertEquals(KIM, player.player)
        assertFalse(player.isIdentity)
    }

    @Test
    fun testCreateAndRefresh() {
        var list = playerListBuilder.create(PLAYERS_BUNDLE)
        assertEquals(11, list?.size)

        var divider = list?.get(0) as PlayerListItem.Divider
        assertEquals("C", (divider as PlayerListItem.Divider.Letter).letter)

        var player = list[1] as PlayerListItem.Player
        assertEquals(CHARLEZARD, player.player)
        assertFalse(player.isIdentity)

        divider = list[2] as PlayerListItem.Divider
        assertEquals("I", (divider as PlayerListItem.Divider.Letter).letter)

        player = list[3] as PlayerListItem.Player
        assertEquals(IMYT, player.player)
        assertFalse(player.isIdentity)

        divider = list[4] as PlayerListItem.Divider
        assertEquals("S", (divider as PlayerListItem.Divider.Letter).letter)

        player = list[5] as PlayerListItem.Player
        assertEquals(SFAT, player.player)
        assertFalse(player.isIdentity)

        player = list[6] as PlayerListItem.Player
        assertEquals(SNAP, player.player)
        assertFalse(player.isIdentity)

        divider = list[7] as PlayerListItem.Divider
        assertTrue(divider is PlayerListItem.Divider.Digit)

        player = list[8] as PlayerListItem.Player
        assertEquals(SIX_LASERS, player.player)
        assertFalse(player.isIdentity)

        divider = list[9] as PlayerListItem.Divider
        assertTrue(divider is PlayerListItem.Divider.Other)

        player = list[10] as PlayerListItem.Player
        assertEquals(KIM, player.player)
        assertFalse(player.isIdentity)

        /////////////////////////////////////////
        // refresh the list and check it again //
        /////////////////////////////////////////

        list = playerListBuilder.refresh(list)
        assertEquals(11, list?.size)

        divider = list?.get(0) as PlayerListItem.Divider
        assertEquals("C", (divider as PlayerListItem.Divider.Letter).letter)

        player = list?.get(1) as PlayerListItem.Player
        assertEquals(CHARLEZARD, player.player)
        assertFalse(player.isIdentity)

        divider = list[2] as PlayerListItem.Divider
        assertEquals("I", (divider as PlayerListItem.Divider.Letter).letter)

        player = list[3] as PlayerListItem.Player
        assertEquals(IMYT, player.player)
        assertFalse(player.isIdentity)

        divider = list[4] as PlayerListItem.Divider
        assertEquals("S", (divider as PlayerListItem.Divider.Letter).letter)

        player = list[5] as PlayerListItem.Player
        assertEquals(SFAT, player.player)
        assertFalse(player.isIdentity)

        player = list[6] as PlayerListItem.Player
        assertEquals(SNAP, player.player)
        assertFalse(player.isIdentity)

        divider = list[7] as PlayerListItem.Divider
        assertTrue(divider is PlayerListItem.Divider.Digit)

        player = list[8] as PlayerListItem.Player
        assertEquals(SIX_LASERS, player.player)
        assertFalse(player.isIdentity)

        divider = list[9] as PlayerListItem.Divider
        assertTrue(divider is PlayerListItem.Divider.Other)

        player = list[10] as PlayerListItem.Player
        assertEquals(KIM, player.player)
        assertFalse(player.isIdentity)
    }

    @Test
    fun testCreateAndRefreshWithImytAsIdentity() {
        var list = playerListBuilder.create(PLAYERS_BUNDLE)
        assertEquals(11, list?.size)

        var divider = list?.get(0) as PlayerListItem.Divider
        assertEquals("C", (divider as PlayerListItem.Divider.Letter).letter)

        var player = list[1] as PlayerListItem.Player
        assertEquals(CHARLEZARD, player.player)
        assertFalse(player.isIdentity)

        divider = list[2] as PlayerListItem.Divider
        assertEquals("I", (divider as PlayerListItem.Divider.Letter).letter)

        player = list[3] as PlayerListItem.Player
        assertEquals(IMYT, player.player)
        assertFalse(player.isIdentity)

        divider = list[4] as PlayerListItem.Divider
        assertEquals("S", (divider as PlayerListItem.Divider.Letter).letter)

        player = list[5] as PlayerListItem.Player
        assertEquals(SFAT, player.player)
        assertFalse(player.isIdentity)

        player = list[6] as PlayerListItem.Player
        assertEquals(SNAP, player.player)
        assertFalse(player.isIdentity)

        divider = list[7] as PlayerListItem.Divider
        assertTrue(divider is PlayerListItem.Divider.Digit)

        player = list[8] as PlayerListItem.Player
        assertEquals(SIX_LASERS, player.player)
        assertFalse(player.isIdentity)

        divider = list[9] as PlayerListItem.Divider
        assertTrue(divider is PlayerListItem.Divider.Other)

        player = list[10] as PlayerListItem.Player
        assertEquals(KIM, player.player)
        assertFalse(player.isIdentity)

        identityRepository.setIdentity(IMYT, NORCAL)
        list = playerListBuilder.refresh(list)
        assertEquals(11, list?.size)

        divider = list?.get(0) as PlayerListItem.Divider
        assertEquals("C", (divider as PlayerListItem.Divider.Letter).letter)

        ////////////////////////////////////////////////////////////////
        // change our identity, then refresh the list and check again //
        ////////////////////////////////////////////////////////////////

        player = list?.get(1) as PlayerListItem.Player
        assertEquals(CHARLEZARD, player.player)
        assertFalse(player.isIdentity)

        divider = list[2] as PlayerListItem.Divider
        assertEquals("I", (divider as PlayerListItem.Divider.Letter).letter)

        player = list[3] as PlayerListItem.Player
        assertEquals(IMYT, player.player)
        assertTrue(player.isIdentity)

        divider = list[4] as PlayerListItem.Divider
        assertEquals("S", (divider as PlayerListItem.Divider.Letter).letter)

        player = list[5] as PlayerListItem.Player
        assertEquals(SFAT, player.player)
        assertFalse(player.isIdentity)

        player = list[6] as PlayerListItem.Player
        assertEquals(SNAP, player.player)
        assertFalse(player.isIdentity)

        divider = list[7] as PlayerListItem.Divider
        assertTrue(divider is PlayerListItem.Divider.Digit)

        player = list[8] as PlayerListItem.Player
        assertEquals(SIX_LASERS, player.player)
        assertFalse(player.isIdentity)

        divider = list[9] as PlayerListItem.Divider
        assertTrue(divider is PlayerListItem.Divider.Other)

        player = list[10] as PlayerListItem.Player
        assertEquals(KIM, player.player)
        assertFalse(player.isIdentity)
    }

    @Test
    fun testCreateWithSnapIsIdentity() {
        identityRepository.setIdentity(SNAP, NORCAL)

        val list = playerListBuilder.create(PLAYERS_BUNDLE)
        assertEquals(11, list?.size)

        var divider = list?.get(0) as PlayerListItem.Divider
        assertEquals("C", (divider as PlayerListItem.Divider.Letter).letter)

        var player = list[1] as PlayerListItem.Player
        assertEquals(CHARLEZARD, player.player)
        assertFalse(player.isIdentity)

        divider = list[2] as PlayerListItem.Divider
        assertEquals("I", (divider as PlayerListItem.Divider.Letter).letter)

        player = list[3] as PlayerListItem.Player
        assertEquals(IMYT, player.player)
        assertFalse(player.isIdentity)

        divider = list[4] as PlayerListItem.Divider
        assertEquals("S", (divider as PlayerListItem.Divider.Letter).letter)

        player = list[5] as PlayerListItem.Player
        assertEquals(SFAT, player.player)
        assertFalse(player.isIdentity)

        player = list[6] as PlayerListItem.Player
        assertEquals(SNAP, player.player)
        assertTrue(player.isIdentity)

        divider = list[7] as PlayerListItem.Divider
        assertTrue(divider is PlayerListItem.Divider.Digit)

        player = list[8] as PlayerListItem.Player
        assertEquals(SIX_LASERS, player.player)
        assertFalse(player.isIdentity)

        divider = list[9] as PlayerListItem.Divider
        assertTrue(divider is PlayerListItem.Divider.Other)

        player = list[10] as PlayerListItem.Player
        assertEquals(KIM, player.player)
        assertFalse(player.isIdentity)
    }

    @Test
    fun testCreateWithEmptyPlayersBundle() {
        assertNull(playerListBuilder.create(EMPTY_PLAYERS_BUNDLE))
    }

    @Test
    fun testCreateWithNull() {
        assertNull(playerListBuilder.create(null))
    }

    @Test
    fun testRefreshWithEmptyList() {
        val emptyList = emptyList<PlayerListItem>()
        val refreshedList = playerListBuilder.refresh(emptyList)
        assertEquals(emptyList.isEmpty(), refreshedList?.isEmpty())
    }

    @Test
    fun testRefreshWithNull() {
        assertNull(playerListBuilder.refresh(null))
    }

    @Test
    fun testSearchWithBlankQueryAndEmptyList() {
        assertNull(playerListBuilder.search(" ", emptyList()))
    }

    @Test
    fun testSearchWithEmptyQueryAndEmptyList() {
        assertNull(playerListBuilder.search("", emptyList()))
    }

    @Test
    fun testSearchWithIAndCharlezardIsIdentity() {
        identityRepository.setIdentity(CHARLEZARD, NORCAL)

        val list = playerListBuilder.create(PLAYERS_BUNDLE)
        val results = playerListBuilder.search("i", list)
        assertEquals(4, results?.size)

        var divider = results?.get(0) as PlayerListItem.Divider
        assertEquals("I", (divider as PlayerListItem.Divider.Letter).letter)

        var player = results[1] as PlayerListItem.Player
        assertEquals(IMYT, player.player)
        assertFalse(player.isIdentity)

        divider = results[2] as PlayerListItem.Divider
        assertTrue(divider is PlayerListItem.Divider.Other)

        player = results[3] as PlayerListItem.Player
        assertEquals(KIM, player.player)
        assertFalse(player.isIdentity)
    }

    @Test
    fun testSearchWithIAndRefreshAndCharlezardIsIdentity() {
        identityRepository.setIdentity(CHARLEZARD, NORCAL)

        val list = playerListBuilder.create(PLAYERS_BUNDLE)
        var results = playerListBuilder.search("i", list)
        assertEquals(4, results?.size)

        var divider = results?.get(0) as PlayerListItem.Divider
        assertEquals("I", (divider as PlayerListItem.Divider.Letter).letter)

        var player = results[1] as PlayerListItem.Player
        assertEquals(IMYT, player.player)
        assertFalse(player.isIdentity)

        divider = results[2] as PlayerListItem.Divider
        assertTrue(divider is PlayerListItem.Divider.Other)

        player = results[3] as PlayerListItem.Player
        assertEquals(KIM, player.player)
        assertFalse(player.isIdentity)

        /////////////////////////////////////////
        // refresh the list and check it again //
        /////////////////////////////////////////

        results = playerListBuilder.refresh(results)
        assertEquals(4, results?.size)

        divider = results?.get(0) as PlayerListItem.Divider
        assertEquals("I", (divider as PlayerListItem.Divider.Letter).letter)

        player = results?.get(1) as PlayerListItem.Player
        assertEquals(IMYT, player.player)
        assertFalse(player.isIdentity)

        divider = results[2] as PlayerListItem.Divider
        assertTrue(divider is PlayerListItem.Divider.Other)

        player = results[3] as PlayerListItem.Player
        assertEquals(KIM, player.player)
        assertFalse(player.isIdentity)
    }

    @Test
    fun testSearchWithSAndSfatAsIdentity() {
        identityRepository.setIdentity(SFAT, NORCAL)

        val list = playerListBuilder.create(PLAYERS_BUNDLE)
        val results = playerListBuilder.search("s", list)
        assertEquals(5, results?.size)

        var divider = results?.get(0) as PlayerListItem.Divider
        assertEquals("S", (divider as PlayerListItem.Divider.Letter).letter)

        var player = results[1] as PlayerListItem.Player
        assertEquals(SFAT, player.player)
        assertTrue(player.isIdentity)

        player = results[2] as PlayerListItem.Player
        assertEquals(SNAP, player.player)
        assertFalse(player.isIdentity)

        divider = results[3] as PlayerListItem.Divider
        assertTrue(divider is PlayerListItem.Divider.Digit)

        player = results[4] as PlayerListItem.Player
        assertEquals(SIX_LASERS, player.player)
        assertFalse(player.isIdentity)
    }

    @Test
    fun testSearchWithZzz() {
        val list = playerListBuilder.create(PLAYERS_BUNDLE)
        val results = playerListBuilder.search(" Zzz ", list)
        assertEquals(1, results?.size)

        val noResults = results?.get(0) as PlayerListItem.NoResults
        assertEquals("Zzz", noResults.query)
    }

    @Test
    fun testSearchWithNullQueryAndNullList() {
        assertNull(playerListBuilder.search(null, null))
    }

}
