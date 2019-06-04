package com.garpr.android.managers

import com.garpr.android.BaseTest
import com.garpr.android.data.models.Avatar
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.Rating
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SmashCharacter
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.features.player.PlayerProfileManager
import com.garpr.android.misc.SmashRosterStorage
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class PlayerProfileManagerTest : BaseTest() {

    @Inject
    protected lateinit var favoritePlayersRepository: FavoritePlayersRepository

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var playerProfileManager: PlayerProfileManager

    @Inject
    protected lateinit var smashRosterStorage: SmashRosterStorage


    companion object {
        private val CHARLEZARD = FullPlayer(
                id = "2",
                name = "Charlezard",
                aliases = listOf("charles"),
                ratings = mapOf(
                        "norcal" to Rating(10f, 2f)
                )
        )

        private val GAR = FullPlayer(
                id = "4",
                name = "gaR",
                aliases = listOf(),
                ratings = mapOf(
                        "norcal" to Rating(20f, 1.5f),
                        "googlemtv" to Rating(25f, 1f)
                )
        )

        private val HAX = FullPlayer(
                id = "3",
                name = "Hax",
                aliases = listOf("hax$"),
                ratings = mapOf(
                        "norcal" to Rating(20f, 1.5f),
                        "nyc" to Rating(25f, 1f)
                )
        )

        private val IMYT = FullPlayer(
                id = "1",
                name = "Imyt"
        )

        private val JAREBAIR = FullPlayer(
                id = "5",
                name = "jarebair"
        )

        private val ATLANTA = Region(null, null,
                null, null,
                "Atlanta", "atlanta", Endpoint.NOT_GAR_PR)

        private val NORCAL = Region(null, null,
                null, null,
                "Norcal", "norcal", Endpoint.GAR_PR)

        private val NYC = Region(null, null,
                null, null,
                "New York City", "nyc", Endpoint.NOT_GAR_PR)

        private val GAR_PR_ROSTER: Map<String, SmashCompetitor> = mapOf(
                CHARLEZARD.id to SmashCompetitor(
                        mains = listOf(
                                SmashCharacter.SHEIK
                        ),
                        websites = mapOf(
                                "other" to "https://github.com/charlesmadere",
                                "twitch" to "https://www.twitch.tv/chillinwithcharles",
                                "twitter" to "https://twitter.com/charlesmadere"
                        ),
                        id = CHARLEZARD.id,
                        name = "Charles Madere",
                        tag = CHARLEZARD.name
                ),

                IMYT.id to SmashCompetitor(
                        mains = listOf(
                                SmashCharacter.SHEIK,
                                SmashCharacter.FOX
                        ),
                        websites = mapOf(
                                "twitch" to "https://www.twitch.tv/imyt",
                                "twitter" to "https://twitter.com/OnlyImyt"
                        ),
                        id = IMYT.id,
                        name = "Declan Doyle",
                        tag = IMYT.name
                ),

                JAREBAIR.id to SmashCompetitor(
                        avatar = Avatar(
                                original = "original.jpg"
                        ),
                        mains = listOf(
                                SmashCharacter.FALCO,
                                SmashCharacter.FOX
                        ),
                        id = JAREBAIR.id,
                        name = "Jared",
                        tag = JAREBAIR.name,
                        websites = mapOf(
                                "other" to "https://theyetee.com/collections/prozd",
                                "twitch" to "https://www.twitch.tv/prozd",
                                "twitter" to "https://twitter.com/prozdkp",
                                "youtube" to "https://www.youtube.com/user/ProZD"
                        )
                )
        )

        private val NOT_GAR_PR_ROSTER: Map<String, SmashCompetitor> = mapOf(
                HAX.id to SmashCompetitor(
                        mains = listOf(
                                SmashCharacter.FOX
                        ),
                        websites = mapOf(
                                "twitter" to "https://twitter.com/ssbmhax",
                                "youtube" to "https://www.youtube.com/channel/UCVJOIYcecIVO96ktK0qDKhQ"
                        ),
                        id = HAX.id,
                        name = "Aziz Al-Yami",
                        tag = HAX.name
                )
        )
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        smashRosterStorage.writeToStorage(Endpoint.GAR_PR, GAR_PR_ROSTER)
        smashRosterStorage.writeToStorage(Endpoint.NOT_GAR_PR, NOT_GAR_PR_ROSTER)
    }

    @Test
    fun testCharlezardWithRoster() {
        val presentation = playerProfileManager.getPresentation(CHARLEZARD, NORCAL)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertFalse(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.mains.isNullOrBlank())
        assertFalse(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertEquals("https://github.com/charlesmadere", presentation.otherWebsite)
        assertEquals("https://www.twitch.tv/chillinwithcharles", presentation.twitch)
        assertEquals("https://twitter.com/charlesmadere", presentation.twitter)
        assertTrue(presentation.youTube.isNullOrBlank())
    }

    @Test
    fun testCharlezardWithoutRoster() {
        smashRosterStorage.deleteFromStorage(Endpoint.GAR_PR)

        val presentation = playerProfileManager.getPresentation(CHARLEZARD, NORCAL)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertFalse(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertTrue(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.otherWebsite.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertTrue(presentation.twitter.isNullOrBlank())
        assertTrue(presentation.youTube.isNullOrBlank())
    }

    @Test
    fun testGar() {
        var presentation = playerProfileManager.getPresentation(GAR, NORCAL)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertTrue(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.otherWebsite.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertTrue(presentation.twitter.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())

        favoritePlayersRepository.addPlayer(GAR, NORCAL)

        presentation = playerProfileManager.getPresentation(GAR, NORCAL)
        assertFalse(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertTrue(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.otherWebsite.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertTrue(presentation.twitter.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())

        favoritePlayersRepository.removePlayer(GAR)

        presentation = playerProfileManager.getPresentation(GAR, NORCAL)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertTrue(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.otherWebsite.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertTrue(presentation.twitter.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())

        identityManager.setIdentity(GAR, NORCAL)

        presentation = playerProfileManager.getPresentation(GAR, NORCAL)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertTrue(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.otherWebsite.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertTrue(presentation.twitter.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())

        favoritePlayersRepository.addPlayer(GAR, NORCAL)

        presentation = playerProfileManager.getPresentation(GAR, NORCAL)
        assertFalse(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertTrue(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.otherWebsite.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertTrue(presentation.twitter.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())

        identityManager.setIdentity(CHARLEZARD, NORCAL)

        presentation = playerProfileManager.getPresentation(GAR, NORCAL)
        assertFalse(presentation.isAddToFavoritesVisible)
        assertTrue(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertTrue(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.otherWebsite.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertTrue(presentation.twitter.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
    }

    @Test
    fun testHaxWithRoster() {
        var presentation = playerProfileManager.getPresentation(HAX, NYC)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertFalse(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.mains.isNullOrBlank())
        assertFalse(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.otherWebsite.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertEquals("https://twitter.com/ssbmhax", presentation.twitter)
        assertEquals("https://www.youtube.com/channel/UCVJOIYcecIVO96ktK0qDKhQ", presentation.youTube)

        presentation = playerProfileManager.getPresentation(HAX, NORCAL)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertFalse(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertTrue(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.otherWebsite.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertTrue(presentation.twitter.isNullOrBlank())
        assertTrue(presentation.youTube.isNullOrBlank())

        presentation = playerProfileManager.getPresentation(HAX, ATLANTA)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertFalse(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.mains.isNullOrBlank())
        assertFalse(presentation.name.isNullOrBlank())
        assertTrue(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertTrue(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.otherWebsite.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertEquals("https://twitter.com/ssbmhax", presentation.twitter)
        assertEquals("https://www.youtube.com/channel/UCVJOIYcecIVO96ktK0qDKhQ", presentation.youTube)
    }

    @Test
    fun testImytWithRoster() {
        val presentation = playerProfileManager.getPresentation(IMYT, NORCAL)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.mains.isNullOrBlank())
        assertFalse(presentation.name.isNullOrBlank())
        assertTrue(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertTrue(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.otherWebsite.isNullOrBlank())
        assertEquals("https://www.twitch.tv/imyt", presentation.twitch)
        assertEquals("https://twitter.com/OnlyImyt", presentation.twitter)
        assertTrue(presentation.youTube.isNullOrBlank())
    }

    @Test
    fun testImytWithoutRoster() {
        smashRosterStorage.deleteFromStorage(Endpoint.GAR_PR)

        val presentation = playerProfileManager.getPresentation(IMYT, NORCAL)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertTrue(presentation.name.isNullOrBlank())
        assertTrue(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertTrue(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.otherWebsite.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertTrue(presentation.twitter.isNullOrBlank())
        assertTrue(presentation.youTube.isNullOrBlank())
    }

    @Test
    fun testJaredWithRoster() {
        val presentation = playerProfileManager.getPresentation(JAREBAIR, NORCAL)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.mains.isNullOrBlank())
        assertFalse(presentation.name.isNullOrBlank())
        assertTrue(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertTrue(presentation.unadjustedRating.isNullOrBlank())
        assertFalse(presentation.avatar.isNullOrBlank())
        assertEquals("https://theyetee.com/collections/prozd", presentation.otherWebsite)
        assertEquals("https://www.twitch.tv/prozd", presentation.twitch)
        assertEquals("https://twitter.com/prozdkp", presentation.twitter)
        assertEquals("https://www.youtube.com/user/ProZD", presentation.youTube)
    }

}
