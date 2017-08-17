package com.garpr.android.misc

import android.app.Application
import com.garpr.android.BaseTest
import com.garpr.android.models.FullPlayer
import com.garpr.android.models.MatchesBundle
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class PlayerToolbarManagerTest : BaseTest() {

    lateinit private var mFullPlayer1: FullPlayer
    lateinit private var mFullPlayer2: FullPlayer
    lateinit private var mMatchesBundle: MatchesBundle

    @Inject
    lateinit protected var mApplication: Application

    @Inject
    lateinit protected var mFavoritePlayersManager: FavoritePlayersManager

    @Inject
    lateinit protected var mGson: Gson

    @Inject
    lateinit protected var mIdentityManager: IdentityManager

    @Inject
    lateinit protected var mPlayerToolbarManager: PlayerToolbarManager

    @Inject
    lateinit protected var mRegionManager: RegionManager


    companion object {
        private const val JSON_FULL_PLAYER_1 = "{\"ratings\":{\"norcal\":{\"mu\":43.23022581617458,\"sigma\":1.180825856678202}},\"name\":\"Laudandus\",\"regions\":[\"norcal\"],\"merge_parent\":null,\"merge_children\":[\"588852e7d2994e3bbfa52d6e\"],\"id\":\"588852e7d2994e3bbfa52d6e\",\"merged\":false,\"aliases\":[\"laudandus\"]}"
        private const val JSON_FULL_PLAYER_2 = "{\"ratings\":{\"norcal\":{\"mu\":37.50924828000645,\"sigma\":0.916403703781555}},\"name\":\"Imyt\",\"regions\":[\"norcal\"],\"merge_parent\":null,\"merge_children\":[\"5877eb55d2994e15c7dea98b\"],\"id\":\"5877eb55d2994e15c7dea98b\",\"merged\":false,\"aliases\":[\"imyt\"]}"
        private const val JSON_MATCHES_BUNDLE = "{\"matches\":[{\"opponent_name\":\"Intersect\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e8d2994e3bbfa52d8b\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"Miles\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e7d2994e3bbfa52d6d\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"BrTarolg\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"587a951dd2994e15c7dea9ec\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"SAB | Ralph\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"lose\",\"opponent_id\":\"588852e8d2994e3bbfa52dcf\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"MIOM | Dr. Z\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e8d2994e3bbfa52d79\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"Reason\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e7d2994e3bbfa52d71\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"NMW\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"583a4a15d2994e0577b05c8a\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"Kalamazhu\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e8d2994e3bbfa52dc5\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"CLG. | SFAT\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"lose\",\"opponent_id\":\"588852e8d2994e3bbfa52d88\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"Zorc\",\"tournament_name\":\"Genesis 4\",\"result\":\"lose\",\"opponent_id\":\"587a951dd2994e15c7dea9e4\",\"tournament_id\":\"58898d7bd2994e6f7981b1c6\",\"tournament_date\":\"01/20/17\"},{\"opponent_name\":\"Berble\",\"tournament_name\":\"Genesis 4\",\"result\":\"win\",\"opponent_id\":\"583a4a15d2994e0577b05c7e\",\"tournament_id\":\"58898d7bd2994e6f7981b1c6\",\"tournament_date\":\"01/20/17\"},{\"opponent_name\":\"darkrain\",\"tournament_name\":\"Genesis 4\",\"result\":\"win\",\"opponent_id\":\"588999c4d2994e713ad63822\",\"tournament_id\":\"58898d7bd2994e6f7981b1c6\",\"tournament_date\":\"01/20/17\"},{\"opponent_name\":\"Frootloop\",\"tournament_name\":\"Genesis 4\",\"result\":\"lose\",\"opponent_id\":\"588852e8d2994e3bbfa52d93\",\"tournament_id\":\"58898d7bd2994e6f7981b1c6\",\"tournament_date\":\"01/20/17\"},{\"opponent_name\":\"GDO | Buttlet\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"588999c5d2994e713ad63a58\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Orion\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"58882bfcd2994e0d53b1458a\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Groovy Green Hat\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"588852e8d2994e3bbfa52dad\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Darrell\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"587a951dd2994e15c7deaa00\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Azusa\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"lose\",\"opponent_id\":\"588852e8d2994e3bbfa52d8c\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Rocky\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"5877eb55d2994e15c7dea982\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Darrell\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"587a951dd2994e15c7deaa00\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"SPY | Nintendude\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"lose\",\"opponent_id\":\"588852e8d2994e3bbfa52da6\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"}],\"player\":{\"id\":\"588852e7d2994e3bbfa52d6e\",\"name\":\"Laudandus\"},\"losses\":18,\"wins\":53}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        mFullPlayer1 = mGson.fromJson(JSON_FULL_PLAYER_1, FullPlayer::class.java)
        mFullPlayer2 = mGson.fromJson(JSON_FULL_PLAYER_2, FullPlayer::class.java)
        mMatchesBundle = mGson.fromJson(JSON_MATCHES_BUNDLE, MatchesBundle::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithOnlyNull() {
        val presentation = mPlayerToolbarManager.getPresentation(mApplication.resources,
                null, null, null)
        assertFalse(presentation.mIsAddToFavoritesVisible)
        assertFalse(presentation.mIsAliasesVisible)
        assertFalse(presentation.mIsFilterVisible)
        assertFalse(presentation.mIsFilterAllVisible)
        assertFalse(presentation.mIsFilterLossesVisible)
        assertFalse(presentation.mIsFilterWinsVisible)
        assertFalse(presentation.mIsRemoveFromFavoritesVisible)
        assertFalse(presentation.mIsSetAsYourIdentityVisible)
        assertFalse(presentation.mIsShareVisible)
        assertFalse(presentation.mIsViewYourselfVsThisOpponentVisible)
        assertNull(presentation.mSetAsYourIdentityTitle)
        assertNull(presentation.mViewYourselfVsThisOpponentTitle)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithFullPlayerAndNullMatchesBundleAndNullResult() {
        var presentation = mPlayerToolbarManager.getPresentation(mApplication.resources,
                mFullPlayer1, null, null)
        assertTrue(presentation.mIsAddToFavoritesVisible)
        assertTrue(presentation.mIsAliasesVisible)
        assertFalse(presentation.mIsFilterVisible)
        assertFalse(presentation.mIsFilterAllVisible)
        assertFalse(presentation.mIsFilterLossesVisible)
        assertFalse(presentation.mIsFilterWinsVisible)
        assertFalse(presentation.mIsRemoveFromFavoritesVisible)
        assertTrue(presentation.mIsSetAsYourIdentityVisible)
        assertTrue(presentation.mIsShareVisible)
        assertFalse(presentation.mIsViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.mSetAsYourIdentityTitle?.isNotBlank() == true)
        assertNull(presentation.mViewYourselfVsThisOpponentTitle)

        mFavoritePlayersManager.addPlayer(mFullPlayer1, mRegionManager.getRegion())

        presentation = mPlayerToolbarManager.getPresentation(mApplication.resources,
                mFullPlayer1, null, null)
        assertTrue(presentation.mIsAliasesVisible)
        assertFalse(presentation.mIsAddToFavoritesVisible)
        assertTrue(presentation.mIsAliasesVisible)
        assertFalse(presentation.mIsFilterVisible)
        assertFalse(presentation.mIsFilterAllVisible)
        assertFalse(presentation.mIsFilterLossesVisible)
        assertFalse(presentation.mIsFilterWinsVisible)
        assertTrue(presentation.mIsRemoveFromFavoritesVisible)
        assertTrue(presentation.mIsSetAsYourIdentityVisible)
        assertTrue(presentation.mIsShareVisible)
        assertFalse(presentation.mIsViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.mSetAsYourIdentityTitle?.isNotBlank() == true)
        assertNull(presentation.mViewYourselfVsThisOpponentTitle)

        mIdentityManager.setIdentity(mFullPlayer2, mRegionManager.getRegion())

        presentation = mPlayerToolbarManager.getPresentation(mApplication.resources,
                mFullPlayer1, null, null)
        assertTrue(presentation.mIsAliasesVisible)
        assertFalse(presentation.mIsAddToFavoritesVisible)
        assertTrue(presentation.mIsAliasesVisible)
        assertFalse(presentation.mIsFilterVisible)
        assertFalse(presentation.mIsFilterAllVisible)
        assertFalse(presentation.mIsFilterLossesVisible)
        assertFalse(presentation.mIsFilterWinsVisible)
        assertTrue(presentation.mIsRemoveFromFavoritesVisible)
        assertFalse(presentation.mIsSetAsYourIdentityVisible)
        assertTrue(presentation.mIsShareVisible)
        assertTrue(presentation.mIsViewYourselfVsThisOpponentVisible)
        assertNull(presentation.mSetAsYourIdentityTitle)
        assertTrue(presentation.mViewYourselfVsThisOpponentTitle?.isNotBlank() == true)

        mFavoritePlayersManager.removePlayer(mFullPlayer1)

        presentation = mPlayerToolbarManager.getPresentation(mApplication.resources,
                mFullPlayer1, null, null)
        assertTrue(presentation.mIsAliasesVisible)
        assertTrue(presentation.mIsAddToFavoritesVisible)
        assertTrue(presentation.mIsAliasesVisible)
        assertFalse(presentation.mIsFilterVisible)
        assertFalse(presentation.mIsFilterAllVisible)
        assertFalse(presentation.mIsFilterLossesVisible)
        assertFalse(presentation.mIsFilterWinsVisible)
        assertFalse(presentation.mIsRemoveFromFavoritesVisible)
        assertFalse(presentation.mIsSetAsYourIdentityVisible)
        assertTrue(presentation.mIsShareVisible)
        assertFalse(presentation.mIsViewYourselfVsThisOpponentVisible)
        assertNull(presentation.mSetAsYourIdentityTitle)
        assertNull(presentation.mViewYourselfVsThisOpponentTitle)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithFullPlayerAndMatchesBundle() {
        var presentation = mPlayerToolbarManager.getPresentation(mApplication.resources,
                mFullPlayer1, mMatchesBundle, null)
        assertTrue(presentation.mIsAddToFavoritesVisible)
        assertTrue(presentation.mIsAliasesVisible)
        assertTrue(presentation.mIsFilterVisible)
        assertFalse(presentation.mIsFilterAllVisible)
        assertTrue(presentation.mIsFilterLossesVisible)
        assertTrue(presentation.mIsFilterWinsVisible)
        assertFalse(presentation.mIsRemoveFromFavoritesVisible)
        assertTrue(presentation.mIsSetAsYourIdentityVisible)
        assertTrue(presentation.mIsShareVisible)
        assertFalse(presentation.mIsViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.mSetAsYourIdentityTitle?.isNotBlank() == true)
        assertNull(presentation.mViewYourselfVsThisOpponentTitle)

        mFavoritePlayersManager.addPlayer(mFullPlayer1, mRegionManager.getRegion())

        presentation = mPlayerToolbarManager.getPresentation(mApplication.resources,
                mFullPlayer1, mMatchesBundle, null)
        assertTrue(presentation.mIsAliasesVisible)
        assertFalse(presentation.mIsAddToFavoritesVisible)
        assertTrue(presentation.mIsAliasesVisible)
        assertTrue(presentation.mIsFilterVisible)
        assertFalse(presentation.mIsFilterAllVisible)
        assertTrue(presentation.mIsFilterLossesVisible)
        assertTrue(presentation.mIsFilterWinsVisible)
        assertTrue(presentation.mIsRemoveFromFavoritesVisible)
        assertTrue(presentation.mIsSetAsYourIdentityVisible)
        assertTrue(presentation.mIsShareVisible)
        assertFalse(presentation.mIsViewYourselfVsThisOpponentVisible)
        assertTrue(presentation.mSetAsYourIdentityTitle?.isNotBlank() == true)
        assertNull(presentation.mViewYourselfVsThisOpponentTitle)

        mIdentityManager.setIdentity(mFullPlayer1, mRegionManager.getRegion())

        presentation = mPlayerToolbarManager.getPresentation(mApplication.resources,
                mFullPlayer1, mMatchesBundle, null)
        assertTrue(presentation.mIsAliasesVisible)
        assertFalse(presentation.mIsAddToFavoritesVisible)
        assertTrue(presentation.mIsAliasesVisible)
        assertTrue(presentation.mIsFilterVisible)
        assertFalse(presentation.mIsFilterAllVisible)
        assertTrue(presentation.mIsFilterLossesVisible)
        assertTrue(presentation.mIsFilterWinsVisible)
        assertTrue(presentation.mIsRemoveFromFavoritesVisible)
        assertFalse(presentation.mIsSetAsYourIdentityVisible)
        assertTrue(presentation.mIsShareVisible)
        assertFalse(presentation.mIsViewYourselfVsThisOpponentVisible)
        assertNull(presentation.mSetAsYourIdentityTitle)
        assertTrue(presentation.mViewYourselfVsThisOpponentTitle?.isNotBlank() == true)

        mFavoritePlayersManager.removePlayer(mFullPlayer1)

        presentation = mPlayerToolbarManager.getPresentation(mApplication.resources,
                mFullPlayer1, mMatchesBundle, null)
        assertTrue(presentation.mIsAliasesVisible)
        assertTrue(presentation.mIsAddToFavoritesVisible)
        assertTrue(presentation.mIsAliasesVisible)
        assertTrue(presentation.mIsFilterVisible)
        assertFalse(presentation.mIsFilterAllVisible)
        assertTrue(presentation.mIsFilterLossesVisible)
        assertTrue(presentation.mIsFilterWinsVisible)
        assertFalse(presentation.mIsRemoveFromFavoritesVisible)
        assertFalse(presentation.mIsSetAsYourIdentityVisible)
        assertTrue(presentation.mIsShareVisible)
        assertFalse(presentation.mIsViewYourselfVsThisOpponentVisible)
        assertNull(presentation.mSetAsYourIdentityTitle)
        assertNull(presentation.mViewYourselfVsThisOpponentTitle)

        presentation = mPlayerToolbarManager.getPresentation(mApplication.resources,
                mFullPlayer2, mMatchesBundle, null)
        assertTrue(presentation.mIsAliasesVisible)
        assertTrue(presentation.mIsAddToFavoritesVisible)
        assertTrue(presentation.mIsAliasesVisible)
        assertTrue(presentation.mIsFilterVisible)
        assertFalse(presentation.mIsFilterAllVisible)
        assertTrue(presentation.mIsFilterLossesVisible)
        assertTrue(presentation.mIsFilterWinsVisible)
        assertTrue(presentation.mIsRemoveFromFavoritesVisible)
        assertFalse(presentation.mIsSetAsYourIdentityVisible)
        assertTrue(presentation.mIsShareVisible)
        assertTrue(presentation.mIsViewYourselfVsThisOpponentVisible)
        assertNull(presentation.mSetAsYourIdentityTitle)
        assertNull(presentation.mViewYourselfVsThisOpponentTitle)
    }

}
