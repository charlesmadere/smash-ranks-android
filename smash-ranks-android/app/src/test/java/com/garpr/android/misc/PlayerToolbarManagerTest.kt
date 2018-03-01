package com.garpr.android.misc

import com.garpr.android.BaseTest
import com.garpr.android.models.FullPlayer
import com.garpr.android.models.MatchResult
import com.garpr.android.models.MatchesBundle
import com.google.gson.Gson
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class PlayerToolbarManagerTest : BaseTest() {

    private lateinit var fullPlayer1: FullPlayer
    private lateinit var fullPlayer2: FullPlayer
    private lateinit var matchesBundle: MatchesBundle

    @Inject
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    @Inject
    protected lateinit var gson: Gson

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var playerToolbarManager: PlayerToolbarManager

    @Inject
    protected lateinit var regionManager: RegionManager


    companion object {
        private const val JSON_FULL_PLAYER_1 = "{\"ratings\":{\"norcal\":{\"mu\":43.23022581617458,\"sigma\":1.180825856678202}},\"name\":\"Laudandus\",\"regions\":[\"norcal\"],\"merge_parent\":null,\"merge_children\":[\"588852e7d2994e3bbfa52d6e\"],\"id\":\"588852e7d2994e3bbfa52d6e\",\"merged\":false,\"aliases\":[\"laudandus\"]}"
        private const val JSON_FULL_PLAYER_2 = "{\"ratings\":{\"norcal\":{\"mu\":37.50924828000645,\"sigma\":0.916403703781555}},\"name\":\"Imyt\",\"regions\":[\"norcal\"],\"merge_parent\":null,\"merge_children\":[\"5877eb55d2994e15c7dea98b\"],\"id\":\"5877eb55d2994e15c7dea98b\",\"merged\":false}"
        private const val JSON_MATCHES_BUNDLE = "{\"matches\":[{\"opponent_name\":\"Intersect\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e8d2994e3bbfa52d8b\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"Miles\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e7d2994e3bbfa52d6d\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"BrTarolg\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"587a951dd2994e15c7dea9ec\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"SAB | Ralph\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"lose\",\"opponent_id\":\"588852e8d2994e3bbfa52dcf\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"MIOM | Dr. Z\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e8d2994e3bbfa52d79\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"Reason\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e7d2994e3bbfa52d71\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"NMW\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"583a4a15d2994e0577b05c8a\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"Kalamazhu\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e8d2994e3bbfa52dc5\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"CLG. | SFAT\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"lose\",\"opponent_id\":\"588852e8d2994e3bbfa52d88\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"Zorc\",\"tournament_name\":\"Genesis 4\",\"result\":\"lose\",\"opponent_id\":\"587a951dd2994e15c7dea9e4\",\"tournament_id\":\"58898d7bd2994e6f7981b1c6\",\"tournament_date\":\"01/20/17\"},{\"opponent_name\":\"Berble\",\"tournament_name\":\"Genesis 4\",\"result\":\"win\",\"opponent_id\":\"583a4a15d2994e0577b05c7e\",\"tournament_id\":\"58898d7bd2994e6f7981b1c6\",\"tournament_date\":\"01/20/17\"},{\"opponent_name\":\"darkrain\",\"tournament_name\":\"Genesis 4\",\"result\":\"win\",\"opponent_id\":\"588999c4d2994e713ad63822\",\"tournament_id\":\"58898d7bd2994e6f7981b1c6\",\"tournament_date\":\"01/20/17\"},{\"opponent_name\":\"Frootloop\",\"tournament_name\":\"Genesis 4\",\"result\":\"lose\",\"opponent_id\":\"588852e8d2994e3bbfa52d93\",\"tournament_id\":\"58898d7bd2994e6f7981b1c6\",\"tournament_date\":\"01/20/17\"},{\"opponent_name\":\"GDO | Buttlet\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"588999c5d2994e713ad63a58\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Orion\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"58882bfcd2994e0d53b1458a\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Groovy Green Hat\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"588852e8d2994e3bbfa52dad\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Darrell\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"587a951dd2994e15c7deaa00\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Azusa\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"lose\",\"opponent_id\":\"588852e8d2994e3bbfa52d8c\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Rocky\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"5877eb55d2994e15c7dea982\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Darrell\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"587a951dd2994e15c7deaa00\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"SPY | Nintendude\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"lose\",\"opponent_id\":\"588852e8d2994e3bbfa52da6\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"}],\"player\":{\"id\":\"588852e7d2994e3bbfa52d6e\",\"name\":\"Laudandus\"},\"losses\":18,\"wins\":53}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        fullPlayer1 = gson.fromJson(JSON_FULL_PLAYER_1, FullPlayer::class.java)
        fullPlayer2 = gson.fromJson(JSON_FULL_PLAYER_2, FullPlayer::class.java)
        matchesBundle = gson.fromJson(JSON_MATCHES_BUNDLE, MatchesBundle::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithOnlyNull() {
        val presentation = playerToolbarManager.getPresentation(null, null,
                null)
        assertFalse(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isAliasesVisible)
        assertFalse(presentation.isFilterVisible)
        assertFalse(presentation.isFilterAllVisible)
        assertFalse(presentation.isFilterLossesVisible)
        assertFalse(presentation.isFilterWinsVisible)
        assertFalse(presentation.isRemoveFromFavoritesVisible)
        assertFalse(presentation.isSetAsYourIdentityVisible)
        assertFalse(presentation.isShareVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithFullPlayerAndNullMatchesBundleAndNullResult() {
        var presentation = playerToolbarManager.getPresentation(fullPlayer1, null,
                null)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertTrue(presentation.isAliasesVisible)
        assertFalse(presentation.isFilterVisible)
        assertFalse(presentation.isFilterAllVisible)
        assertFalse(presentation.isFilterLossesVisible)
        assertFalse(presentation.isFilterWinsVisible)
        assertFalse(presentation.isRemoveFromFavoritesVisible)
        assertTrue(presentation.isSetAsYourIdentityVisible)
        assertTrue(presentation.isShareVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)

        favoritePlayersManager.addPlayer(fullPlayer1, regionManager.getRegion())

        presentation = playerToolbarManager.getPresentation(fullPlayer1, null,
                null)
        assertTrue(presentation.isAliasesVisible)
        assertFalse(presentation.isAddToFavoritesVisible)
        assertTrue(presentation.isAliasesVisible)
        assertFalse(presentation.isFilterVisible)
        assertFalse(presentation.isFilterAllVisible)
        assertFalse(presentation.isFilterLossesVisible)
        assertFalse(presentation.isFilterWinsVisible)
        assertTrue(presentation.isRemoveFromFavoritesVisible)
        assertTrue(presentation.isSetAsYourIdentityVisible)
        assertTrue(presentation.isShareVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)

        identityManager.setIdentity(fullPlayer2, regionManager.getRegion())

        presentation = playerToolbarManager.getPresentation(fullPlayer1, null,
                null)
        assertTrue(presentation.isAliasesVisible)
        assertFalse(presentation.isAddToFavoritesVisible)
        assertTrue(presentation.isAliasesVisible)
        assertFalse(presentation.isFilterVisible)
        assertFalse(presentation.isFilterAllVisible)
        assertFalse(presentation.isFilterLossesVisible)
        assertFalse(presentation.isFilterWinsVisible)
        assertTrue(presentation.isRemoveFromFavoritesVisible)
        assertFalse(presentation.isSetAsYourIdentityVisible)
        assertTrue(presentation.isShareVisible)
        assertTrue(presentation.isViewYourselfVsThisOpponentVisible)

        favoritePlayersManager.removePlayer(fullPlayer1)

        presentation = playerToolbarManager.getPresentation(fullPlayer1, null,
                null)
        assertTrue(presentation.isAliasesVisible)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertTrue(presentation.isAliasesVisible)
        assertFalse(presentation.isFilterVisible)
        assertFalse(presentation.isFilterAllVisible)
        assertFalse(presentation.isFilterLossesVisible)
        assertFalse(presentation.isFilterWinsVisible)
        assertFalse(presentation.isRemoveFromFavoritesVisible)
        assertFalse(presentation.isSetAsYourIdentityVisible)
        assertTrue(presentation.isShareVisible)
        assertTrue(presentation.isViewYourselfVsThisOpponentVisible)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithFullPlayerAndMatchesBundleAndNullResult() {
        var presentation = playerToolbarManager.getPresentation(fullPlayer1, matchesBundle,
                null)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertTrue(presentation.isAliasesVisible)
        assertTrue(presentation.isFilterVisible)
        assertFalse(presentation.isFilterAllVisible)
        assertTrue(presentation.isFilterLossesVisible)
        assertTrue(presentation.isFilterWinsVisible)
        assertFalse(presentation.isRemoveFromFavoritesVisible)
        assertTrue(presentation.isSetAsYourIdentityVisible)
        assertTrue(presentation.isShareVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)

        favoritePlayersManager.addPlayer(fullPlayer1, regionManager.getRegion())

        presentation = playerToolbarManager.getPresentation(fullPlayer1, matchesBundle,
                null)
        assertTrue(presentation.isAliasesVisible)
        assertFalse(presentation.isAddToFavoritesVisible)
        assertTrue(presentation.isAliasesVisible)
        assertTrue(presentation.isFilterVisible)
        assertFalse(presentation.isFilterAllVisible)
        assertTrue(presentation.isFilterLossesVisible)
        assertTrue(presentation.isFilterWinsVisible)
        assertTrue(presentation.isRemoveFromFavoritesVisible)
        assertTrue(presentation.isSetAsYourIdentityVisible)
        assertTrue(presentation.isShareVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)

        identityManager.setIdentity(fullPlayer1, regionManager.getRegion())

        presentation = playerToolbarManager.getPresentation(fullPlayer1, matchesBundle,
                null)
        assertTrue(presentation.isAliasesVisible)
        assertFalse(presentation.isAddToFavoritesVisible)
        assertTrue(presentation.isAliasesVisible)
        assertTrue(presentation.isFilterVisible)
        assertFalse(presentation.isFilterAllVisible)
        assertTrue(presentation.isFilterLossesVisible)
        assertTrue(presentation.isFilterWinsVisible)
        assertTrue(presentation.isRemoveFromFavoritesVisible)
        assertFalse(presentation.isSetAsYourIdentityVisible)
        assertTrue(presentation.isShareVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)

        favoritePlayersManager.removePlayer(fullPlayer1)

        presentation = playerToolbarManager.getPresentation(fullPlayer1, matchesBundle,
                null)
        assertTrue(presentation.isAliasesVisible)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertTrue(presentation.isAliasesVisible)
        assertTrue(presentation.isFilterVisible)
        assertFalse(presentation.isFilterAllVisible)
        assertTrue(presentation.isFilterLossesVisible)
        assertTrue(presentation.isFilterWinsVisible)
        assertFalse(presentation.isRemoveFromFavoritesVisible)
        assertFalse(presentation.isSetAsYourIdentityVisible)
        assertTrue(presentation.isShareVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)

        presentation = playerToolbarManager.getPresentation(fullPlayer2, matchesBundle,
                null)
        assertFalse(presentation.isAliasesVisible)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isAliasesVisible)
        assertTrue(presentation.isFilterVisible)
        assertFalse(presentation.isFilterAllVisible)
        assertTrue(presentation.isFilterLossesVisible)
        assertTrue(presentation.isFilterWinsVisible)
        assertFalse(presentation.isRemoveFromFavoritesVisible)
        assertFalse(presentation.isSetAsYourIdentityVisible)
        assertTrue(presentation.isShareVisible)
        assertTrue(presentation.isViewYourselfVsThisOpponentVisible)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithFullPlayerAndMatchesBundleAndLoseResult() {
        var presentation = playerToolbarManager.getPresentation(fullPlayer1, matchesBundle,
                MatchResult.LOSE)
        assertTrue(presentation.isAliasesVisible)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertTrue(presentation.isAliasesVisible)
        assertTrue(presentation.isFilterVisible)
        assertTrue(presentation.isFilterAllVisible)
        assertFalse(presentation.isFilterLossesVisible)
        assertTrue(presentation.isFilterWinsVisible)
        assertFalse(presentation.isRemoveFromFavoritesVisible)
        assertTrue(presentation.isShareVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)

        presentation = playerToolbarManager.getPresentation(fullPlayer2, matchesBundle,
                MatchResult.LOSE)
        assertFalse(presentation.isAliasesVisible)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isAliasesVisible)
        assertTrue(presentation.isFilterVisible)
        assertTrue(presentation.isFilterAllVisible)
        assertFalse(presentation.isFilterLossesVisible)
        assertTrue(presentation.isFilterWinsVisible)
        assertFalse(presentation.isRemoveFromFavoritesVisible)
        assertTrue(presentation.isShareVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithFullPlayerAndMatchesBundleAndWinResult() {
        var presentation = playerToolbarManager.getPresentation(fullPlayer1, matchesBundle,
                MatchResult.WIN)
        assertTrue(presentation.isAliasesVisible)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertTrue(presentation.isAliasesVisible)
        assertTrue(presentation.isFilterVisible)
        assertTrue(presentation.isFilterAllVisible)
        assertTrue(presentation.isFilterLossesVisible)
        assertFalse(presentation.isFilterWinsVisible)
        assertFalse(presentation.isRemoveFromFavoritesVisible)
        assertTrue(presentation.isShareVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)

        presentation = playerToolbarManager.getPresentation(fullPlayer2, matchesBundle,
                MatchResult.WIN)
        assertFalse(presentation.isAliasesVisible)
        assertTrue(presentation.isAddToFavoritesVisible)
        assertFalse(presentation.isAliasesVisible)
        assertTrue(presentation.isFilterVisible)
        assertTrue(presentation.isFilterAllVisible)
        assertTrue(presentation.isFilterLossesVisible)
        assertFalse(presentation.isFilterWinsVisible)
        assertFalse(presentation.isRemoveFromFavoritesVisible)
        assertTrue(presentation.isShareVisible)
        assertFalse(presentation.isViewYourselfVsThisOpponentVisible)
    }

}
