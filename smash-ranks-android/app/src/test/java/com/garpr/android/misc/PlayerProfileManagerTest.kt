package com.garpr.android.misc

import com.garpr.android.BaseTest
import com.garpr.android.models.AbsRegion
import com.garpr.android.models.FullPlayer
import com.garpr.android.models.LiteRegion
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
        private val FULL_PLAYER_1 = FullPlayer("1", "Charlezard", listOf("charles"),
                null, null)
        private val FULL_PLAYER_2 = FullPlayer("2", "Imyt", null,
                null, null)

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

}
