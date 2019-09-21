package com.garpr.android.features.player

import com.garpr.android.BaseTest
import com.garpr.android.data.models.Avatar
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.Rating
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SmashCharacter
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.repositories.IdentityRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PlayerProfileManagerTest : BaseTest() {

    protected val identityRepository: IdentityRepository by inject()
    protected val playerProfileManager: PlayerProfileManager by inject()

    companion object {

        private object Players {
            val CHARLEZARD = FullPlayer(
                    id = "2",
                    name = "Charlezard",
                    aliases = listOf("charles"),
                    ratings = mapOf(
                            "norcal" to Rating(10f, 2f)
                    )
            )

            val GAR = FullPlayer(
                    id = "4",
                    name = "gaR",
                    aliases = emptyList(),
                    ratings = mapOf(
                            "norcal" to Rating(20f, 1.5f),
                            "googlemtv" to Rating(25f, 1f)
                    )
            )

            val HAX = FullPlayer(
                    id = "3",
                    name = "Hax",
                    aliases = listOf("hax$"),
                    ratings = mapOf(
                            "norcal" to Rating(20f, 1.5f),
                            "nyc" to Rating(25f, 1f)
                    )
            )

            val IMYT = FullPlayer(
                    id = "1",
                    name = "Imyt"
            )

            val JAREBAIR = FullPlayer(
                    id = "5",
                    name = "jarebair"
            )
        }

        private object Regions {
            val ATLANTA = Region(
                    displayName = "Atlanta",
                    id = "atlanta",
                    endpoint = Endpoint.NOT_GAR_PR
            )

            val NORCAL = Region(
                    displayName = "Norcal",
                    id = "norcal",
                    endpoint = Endpoint.GAR_PR
            )

            val NYC = Region(
                    displayName = "New York City",
                    id = "nyc",
                    endpoint = Endpoint.NOT_GAR_PR
            )
        }

        private object SmashCompetitors {
            val CHARLEZARD = SmashCompetitor(
                    mains = listOf(
                            SmashCharacter.SHEIK
                    ),
                    websites = mapOf(
                            "twitch" to "https://www.twitch.tv/chillinwithcharles",
                            "twitter" to "https://twitter.com/charlesmadere"
                    ),
                    id = Players.CHARLEZARD.id,
                    name = "Charles Madere",
                    tag = Players.CHARLEZARD.name
            )

            val HAX = SmashCompetitor(
                    mains = listOf(
                            SmashCharacter.FOX
                    ),
                    websites = mapOf(
                            "twitter" to "https://twitter.com/ssbmhax",
                            "youtube" to "https://www.youtube.com/channel/UCVJOIYcecIVO96ktK0qDKhQ"
                    ),
                    id = Players.HAX.id,
                    name = "Aziz Al-Yami",
                    tag = Players.HAX.name
            )

            val IMYT = SmashCompetitor(
                    mains = listOf(
                            SmashCharacter.SHEIK,
                            SmashCharacter.FOX
                    ),
                    websites = mapOf(
                            "twitch" to "https://www.twitch.tv/imyt",
                            "twitter" to "https://twitter.com/OnlyImyt"
                    ),
                    id = Players.IMYT.id,
                    name = "Declan Doyle",
                    tag = Players.IMYT.name
            )

            val JAREBAIR = SmashCompetitor(
                    avatar = Avatar(
                            original = "original.jpg"
                    ),
                    mains = listOf(
                            SmashCharacter.FALCO,
                            SmashCharacter.FOX
                    ),
                    id = Players.JAREBAIR.id,
                    name = "Jared",
                    tag = Players.JAREBAIR.name,
                    websites = mapOf(
                            "twitch" to "https://www.twitch.tv/prozd",
                            "twitter" to "https://twitter.com/prozdkp",
                            "youtube" to "https://www.youtube.com/user/ProZD"
                    )
            )
        }

    }

    @Test
    fun testCharlezardWithRoster() {
        val presentation = playerProfileManager.getPresentation(
                region = Regions.NORCAL,
                isFavorited = false,
                player = Players.CHARLEZARD,
                competitor = SmashCompetitors.CHARLEZARD
        )

        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isCompareVisible)
        assertFalse(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.mains.isNullOrBlank())
        assertFalse(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertEquals("https://www.twitch.tv/chillinwithcharles", presentation.twitch)
        assertEquals("https://twitter.com/charlesmadere", presentation.twitter)
        assertTrue(presentation.youTube.isNullOrBlank())
    }

    @Test
    fun testCharlezardWithoutRoster() {
        val presentation = playerProfileManager.getPresentation(
                region = Regions.NORCAL,
                isFavorited = false,
                player = Players.CHARLEZARD
        )

        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isCompareVisible)
        assertFalse(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertTrue(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertTrue(presentation.twitter.isNullOrBlank())
        assertTrue(presentation.youTube.isNullOrBlank())
    }

    @Test
    fun testGarInFavoritesAndNoIdentity() {
        val presentation = playerProfileManager.getPresentation(
                region = Regions.NORCAL,
                isFavorited = true,
                player = Players.GAR
        )

        assertFalse(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isCompareVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertTrue(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertTrue(presentation.twitter.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
    }

    @Test
    fun testGarInFavoritesAndCharlezardIsIdentity() {
        identityRepository.setIdentity(Players.CHARLEZARD, Regions.NORCAL)
        val presentation = playerProfileManager.getPresentation(
                region = Regions.NORCAL,
                isFavorited = true,
                player = Players.GAR
        )

        assertFalse(presentation.isAddToFavoritesVisible)
        assertTrue(presentation.isCompareVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertTrue(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertTrue(presentation.twitter.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
    }

    @Test
    fun testGarInFavoritesAndGarIsIdentity() {
        identityRepository.setIdentity(Players.GAR, Regions.NORCAL)
        val presentation = playerProfileManager.getPresentation(
                region = Regions.NORCAL,
                isFavorited = true,
                player = Players.GAR
        )

        assertFalse(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isCompareVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertTrue(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertTrue(presentation.twitter.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
    }

    @Test
    fun testGarNotInFavoritesAndGarIsIdentity() {
        identityRepository.setIdentity(Players.GAR, Regions.NORCAL)
        val presentation = playerProfileManager.getPresentation(
                region = Regions.NORCAL,
                isFavorited = false,
                player = Players.GAR
        )

        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isCompareVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertTrue(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertTrue(presentation.twitter.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
    }

    @Test
    fun testGarNotInFavoritesAndNoIdentity() {
        val presentation = playerProfileManager.getPresentation(
                region = Regions.NORCAL,
                isFavorited = false,
                player = Players.GAR
        )

        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isCompareVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertTrue(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertTrue(presentation.twitter.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
    }

    @Test
    fun testHax() {
        var presentation = playerProfileManager.getPresentation(
                region = Regions.NYC,
                isFavorited = false,
                player = Players.HAX,
                competitor = SmashCompetitors.HAX
        )

        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isCompareVisible)
        assertFalse(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.mains.isNullOrBlank())
        assertFalse(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertEquals("https://twitter.com/ssbmhax", presentation.twitter)
        assertEquals("https://www.youtube.com/channel/UCVJOIYcecIVO96ktK0qDKhQ", presentation.youTube)

        presentation = playerProfileManager.getPresentation(
                region = Regions.NORCAL,
                isFavorited = false,
                player = Players.HAX
        )

        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isCompareVisible)
        assertFalse(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertTrue(presentation.name.isNullOrBlank())
        assertFalse(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertFalse(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertTrue(presentation.twitter.isNullOrBlank())
        assertTrue(presentation.youTube.isNullOrBlank())

        presentation = playerProfileManager.getPresentation(
                region = Regions.ATLANTA,
                isFavorited = false,
                player = Players.HAX,
                competitor = SmashCompetitors.HAX
        )

        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isCompareVisible)
        assertFalse(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.mains.isNullOrBlank())
        assertFalse(presentation.name.isNullOrBlank())
        assertTrue(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertTrue(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertEquals("https://twitter.com/ssbmhax", presentation.twitter)
        assertEquals("https://www.youtube.com/channel/UCVJOIYcecIVO96ktK0qDKhQ", presentation.youTube)
    }

    @Test
    fun testImytWithRoster() {
        val presentation = playerProfileManager.getPresentation(
                region = Regions.NORCAL,
                isFavorited = false,
                player = Players.IMYT,
                competitor = SmashCompetitors.IMYT
        )

        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isCompareVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.mains.isNullOrBlank())
        assertFalse(presentation.name.isNullOrBlank())
        assertTrue(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertTrue(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertEquals("https://www.twitch.tv/imyt", presentation.twitch)
        assertEquals("https://twitter.com/OnlyImyt", presentation.twitter)
        assertTrue(presentation.youTube.isNullOrBlank())
    }

    @Test
    fun testImytWithoutRoster() {
        val presentation = playerProfileManager.getPresentation(
                region = Regions.NORCAL,
                isFavorited = false,
                player = Players.IMYT
        )

        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isCompareVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.mains.isNullOrBlank())
        assertTrue(presentation.name.isNullOrBlank())
        assertTrue(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertTrue(presentation.unadjustedRating.isNullOrBlank())
        assertTrue(presentation.avatar.isNullOrBlank())
        assertTrue(presentation.twitch.isNullOrBlank())
        assertTrue(presentation.twitter.isNullOrBlank())
        assertTrue(presentation.youTube.isNullOrBlank())
    }

    @Test
    fun testJaredWithRoster() {
        val presentation = playerProfileManager.getPresentation(
                region = Regions.NORCAL,
                isFavorited = false,
                player = Players.JAREBAIR,
                competitor = SmashCompetitors.JAREBAIR
        )

        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isCompareVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertFalse(presentation.mains.isNullOrBlank())
        assertFalse(presentation.name.isNullOrBlank())
        assertTrue(presentation.rating.isNullOrBlank())
        assertFalse(presentation.tag.isBlank())
        assertTrue(presentation.unadjustedRating.isNullOrBlank())
        assertFalse(presentation.avatar.isNullOrBlank())
        assertEquals("https://www.twitch.tv/prozd", presentation.twitch)
        assertEquals("https://twitter.com/prozdkp", presentation.twitter)
        assertEquals("https://www.youtube.com/user/ProZD", presentation.youTube)
    }

}
