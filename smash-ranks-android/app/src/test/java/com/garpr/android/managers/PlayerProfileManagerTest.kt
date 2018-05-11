package com.garpr.android.managers

import com.garpr.android.BaseTest
import com.garpr.android.misc.SmashRosterStorage
import com.garpr.android.models.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class PlayerProfileManagerTest : BaseTest() {

    @Inject
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var playerProfileManager: PlayerProfileManager

    @Inject
    protected lateinit var smashRosterStorage: SmashRosterStorage


    companion object {
        private val FULL_PLAYER_1 = FullPlayer("1", "Imyt", null,
                null, null)

        private val FULL_PLAYER_2 = FullPlayer("2", "Charlezard", listOf("charles"),
                null, mapOf(
                        "norcal" to Rating(10f, 2f)
        ))

        private val FULL_PLAYER_3 = FullPlayer("3", "Hax", listOf("hax$"),
                null, mapOf(
                    "norcal" to Rating(20f, 1.5f),
                    "nyc" to Rating(25f, 1f)
        ))

        private val FULL_PLAYER_4 = FullPlayer("4", "gaR", listOf(),
                null, mapOf(
                    "norcal" to Rating(20f, 1.5f),
                    "googlemtv" to Rating(25f, 1f)
        ))

        private val REGION_1 = Region(null, null,
                null, null, "Norcal",
                "norcal", Endpoint.GAR_PR)

        private val REGION_2 = Region(null, null,
                null, null, "New York City",
                "nyc", Endpoint.NOT_GAR_PR)

        private val REGION_3 = Region(null, null,
                null, null, "Atlanta",
                "atlanta", Endpoint.NOT_GAR_PR)

        private val SMASH_ROSTER_1 = SmashRoster(
                mapOf(
                        FULL_PLAYER_1.id to SmashCompetitor(
                                mains = listOf(
                                        SmashCharacter.SHEIK,
                                        SmashCharacter.FOX
                                ),
                                websites = mapOf(
                                        "twitch" to "https://www.twitch.tv/imyt",
                                        "twitter" to "https://twitter.com/OnlyImyt"
                                ),
                                id = FULL_PLAYER_1.id,
                                name = "Declan Doyle",
                                tag = FULL_PLAYER_1.name
                        ),

                        FULL_PLAYER_2.id to SmashCompetitor(
                                mains = listOf(
                                        SmashCharacter.SHEIK
                                ),
                                websites = mapOf(
                                        "other" to "http://charlesmadere.com",
                                        "twitch" to "https://www.twitch.tv/imyt",
                                        "twitter" to "https://twitter.com/OnlyImyt"
                                ),
                                id = FULL_PLAYER_2.id,
                                name = "Charles Madere",
                                tag = FULL_PLAYER_2.name
                        )
                )
        )
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    fun testFullPlayer1() {
        val presentation = playerProfileManager.getPresentation(FULL_PLAYER_1, REGION_1)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertTrue(presentation.unadjustedRating.isNullOrBlank())
    }

    @Test
    fun testFullPlayer1WithSmashCompetitor() {
        smashRosterStorage.writeToStorage(REGION_1, SMASH_ROSTER_1)
        val presentation = playerProfileManager.getPresentation(FULL_PLAYER_1, REGION_1)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertFalse(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.mains.isNullOrBlank())
        assertFalse(presentation.name.isNullOrBlank())
        assertTrue(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertTrue(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.otherWebsite.isNullOrBlank())
        assertEquals(presentation.twitch, "https://www.twitch.tv/imyt")
        assertEquals(presentation.twitter, "https://twitter.com/OnlyImyt")
        assertTrue(presentation.youTube.isNullOrBlank())
    }

    @Test
    fun testFullPlayer2WithSmashCompetitor() {
        smashRosterStorage.writeToStorage(REGION_1, SMASH_ROSTER_1)
        val presentation = playerProfileManager.getPresentation(FULL_PLAYER_2, REGION_1)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertFalse(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertFalse(presentation.name.isNullOrBlank())
        assertTrue(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertTrue(presentation.unadjustedRating.isNullOrBlank())
        assertEquals(presentation.otherWebsite, "http://charlesmadere.com/")
        assertEquals(presentation.twitch, "https://www.twitch.tv/chillinwithcharles")
        assertEquals(presentation.twitter, "https://twitter.com/charlesmadere")
        assertTrue(presentation.youTube.isNullOrBlank())
    }

    @Test
    fun testFullPlayer2() {
        val presentation = playerProfileManager.getPresentation(FULL_PLAYER_2, REGION_2)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertFalse(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertTrue(presentation.unadjustedRating.isNullOrBlank())
    }

    @Test
    fun testFullPlayer3() {
        var presentation = playerProfileManager.getPresentation(FULL_PLAYER_3, REGION_2)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertFalse(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())

        presentation = playerProfileManager.getPresentation(FULL_PLAYER_3, REGION_1)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertFalse(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())

        presentation = playerProfileManager.getPresentation(FULL_PLAYER_3, REGION_3)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertFalse(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertTrue(presentation.unadjustedRating.isNullOrBlank())
    }

    @Test
    fun testFullPlayer4() {
        var presentation = playerProfileManager.getPresentation(FULL_PLAYER_4, REGION_1)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())

        favoritePlayersManager.addPlayer(FULL_PLAYER_4, REGION_1)

        presentation = playerProfileManager.getPresentation(FULL_PLAYER_4, REGION_1)
        assertFalse(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())

        favoritePlayersManager.removePlayer(FULL_PLAYER_4)

        presentation = playerProfileManager.getPresentation(FULL_PLAYER_4, REGION_1)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())

        identityManager.setIdentity(FULL_PLAYER_4, REGION_1)

        presentation = playerProfileManager.getPresentation(FULL_PLAYER_4, REGION_1)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())

        favoritePlayersManager.addPlayer(FULL_PLAYER_4, REGION_1)

        presentation = playerProfileManager.getPresentation(FULL_PLAYER_4, REGION_1)
        assertFalse(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
    }

}
