package com.garpr.android.misc

import com.garpr.android.BaseTest
import com.garpr.android.models.AbsRegion
import com.garpr.android.models.FullPlayer
import com.garpr.android.models.LiteRegion
import com.garpr.android.models.Rating
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
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var playerProfileManager: PlayerProfileManager


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

        private val REGION_1: AbsRegion = LiteRegion(null, null,
                null, null, "Norcal",
                "norcal")
        private val REGION_2: AbsRegion = LiteRegion(null, null,
                null, null, "New York City",
                "nyc")
    }

    @Before
    @Throws(Exception::class)
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
        assertTrue(presentation.unadjustedRating.isNullOrBlank())
    }

    @Test
    fun testFullPlayer2() {
        val presentation = playerProfileManager.getPresentation(FULL_PLAYER_2, REGION_1)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.rating.isNullOrBlank())
        assertTrue(presentation.unadjustedRating.isNullOrBlank())
    }

    @Test
    fun testFullPlayer3() {
        val presentation = playerProfileManager.getPresentation(FULL_PLAYER_3, REGION_2)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.rating.isNullOrBlank())
        assertTrue(presentation.unadjustedRating.isNullOrBlank())
    }

    @Test
    fun testFullPlayer4() {
        val presentation = playerProfileManager.getPresentation(FULL_PLAYER_4, REGION_1)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.aliases.isNullOrBlank())
        assertTrue(presentation.rating.isNullOrBlank())
        assertTrue(presentation.unadjustedRating.isNullOrBlank())
    }

}
