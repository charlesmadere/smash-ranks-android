package com.garpr.android.data.database

import com.garpr.android.data.models.Avatar
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SmashCharacter
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.misc.Constants
import com.garpr.android.test.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DbSmashCompetitorTest : BaseTest() {

    @Test
    fun testConstructFromSmashCompetitorWithHax() {
        val hax = DbSmashCompetitor(
                smashCompetitor = HAX,
                endpoint = NYC.endpoint
        )

        assertEquals(HAX.avatar, hax.avatar)
        assertEquals(NYC.endpoint, hax.endpoint)
        assertEquals(HAX.mains, hax.mains)
        assertEquals(HAX.websites, hax.websites)
        assertEquals(HAX.id, hax.id)
        assertEquals(HAX.name, hax.name)
        assertEquals(HAX.tag, hax.tag)
    }

    @Test
    fun testConstructFromSmashCompetitorWithImyt() {
        val imyt = DbSmashCompetitor(
                smashCompetitor = IMYT,
                endpoint = NORCAL.endpoint
        )

        assertEquals(IMYT.avatar, imyt.avatar)
        assertEquals(NORCAL.endpoint, imyt.endpoint)
        assertEquals(IMYT.mains, imyt.mains)
        assertEquals(IMYT.websites, imyt.websites)
        assertEquals(IMYT.id, imyt.id)
        assertEquals(IMYT.name, imyt.name)
        assertEquals(IMYT.tag, imyt.tag)
    }

    @Test
    fun testToSmashCompetitorWithHax() {
        val smashCompetitor = DB_HAX.toSmashCompetitor()
        assertEquals(DB_HAX.avatar, smashCompetitor.avatar)
        assertEquals(DB_HAX.mains, smashCompetitor.mains)
        assertEquals(DB_HAX.websites, smashCompetitor.websites)
        assertEquals(DB_HAX.id, smashCompetitor.id)
        assertEquals(DB_HAX.name, smashCompetitor.name)
        assertEquals(DB_HAX.tag, smashCompetitor.tag)
    }

    @Test
    fun testToSmashCompetitorWithImyt() {
        val smashCompetitor = DB_IMYT.toSmashCompetitor()
        assertEquals(DB_IMYT.avatar, smashCompetitor.avatar)
        assertEquals(DB_IMYT.mains, smashCompetitor.mains)
        assertEquals(DB_IMYT.websites, smashCompetitor.websites)
        assertEquals(DB_IMYT.id, smashCompetitor.id)
        assertEquals(DB_IMYT.name, smashCompetitor.name)
        assertEquals(DB_IMYT.tag, smashCompetitor.tag)
    }

    companion object {
        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val NYC = Region(
                displayName = "New York City",
                id = "nyc",
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val HAX = SmashCompetitor(
                avatar = Avatar(original = "hax_avatar.jpg"),
                mains = listOf(SmashCharacter.FOX),
                websites = mapOf(Constants.YOUTUBE to "https://www.youtube.com/channel/UCVJOIYcecIVO96ktK0qDKhQ"),
                id = "53c64dba8ab65f6e6651f7bc",
                name = "Aziz",
                tag = "Hax$"
        )

        private val IMYT = SmashCompetitor(
                avatar = Avatar(original = "imyt_avatar.png"),
                mains = listOf(SmashCharacter.SHEIK, SmashCharacter.FALCO),
                websites = mapOf(Constants.TWITCH to "https://twitch.tv/imyt"),
                id = "5877eb55d2994e15c7dea98b",
                name = "Declan",
                tag = "Imyt"
        )

        private val DB_HAX = DbSmashCompetitor(
                avatar = HAX.avatar,
                endpoint = NYC.endpoint,
                mains = HAX.mains,
                websites = HAX.websites,
                id = HAX.id,
                name = HAX.name,
                tag = HAX.tag
        )

        private val DB_IMYT = DbSmashCompetitor(
                avatar = IMYT.avatar,
                endpoint = NORCAL.endpoint,
                mains = IMYT.mains,
                websites = IMYT.websites,
                id = IMYT.id,
                name = IMYT.name,
                tag = IMYT.tag
        )
    }

}
