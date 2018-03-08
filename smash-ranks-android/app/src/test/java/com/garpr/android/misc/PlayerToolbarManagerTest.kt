package com.garpr.android.misc

import com.garpr.android.BaseTest
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

    private lateinit var matchesBundle: MatchesBundle

    @Inject
    protected lateinit var gson: Gson

    @Inject
    protected lateinit var playerToolbarManager: PlayerToolbarManager


    companion object {
        private const val JSON_MATCHES_BUNDLE = "{\"matches\":[{\"opponent_name\":\"Intersect\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e8d2994e3bbfa52d8b\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"Miles\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e7d2994e3bbfa52d6d\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"BrTarolg\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"587a951dd2994e15c7dea9ec\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"SAB | Ralph\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"lose\",\"opponent_id\":\"588852e8d2994e3bbfa52dcf\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"MIOM | Dr. Z\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e8d2994e3bbfa52d79\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"Reason\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e7d2994e3bbfa52d71\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"NMW\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"583a4a15d2994e0577b05c8a\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"Kalamazhu\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e8d2994e3bbfa52dc5\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"CLG. | SFAT\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"lose\",\"opponent_id\":\"588852e8d2994e3bbfa52d88\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"},{\"opponent_name\":\"Zorc\",\"tournament_name\":\"Genesis 4\",\"result\":\"lose\",\"opponent_id\":\"587a951dd2994e15c7dea9e4\",\"tournament_id\":\"58898d7bd2994e6f7981b1c6\",\"tournament_date\":\"01/20/17\"},{\"opponent_name\":\"Berble\",\"tournament_name\":\"Genesis 4\",\"result\":\"win\",\"opponent_id\":\"583a4a15d2994e0577b05c7e\",\"tournament_id\":\"58898d7bd2994e6f7981b1c6\",\"tournament_date\":\"01/20/17\"},{\"opponent_name\":\"darkrain\",\"tournament_name\":\"Genesis 4\",\"result\":\"win\",\"opponent_id\":\"588999c4d2994e713ad63822\",\"tournament_id\":\"58898d7bd2994e6f7981b1c6\",\"tournament_date\":\"01/20/17\"},{\"opponent_name\":\"Frootloop\",\"tournament_name\":\"Genesis 4\",\"result\":\"lose\",\"opponent_id\":\"588852e8d2994e3bbfa52d93\",\"tournament_id\":\"58898d7bd2994e6f7981b1c6\",\"tournament_date\":\"01/20/17\"},{\"opponent_name\":\"GDO | Buttlet\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"588999c5d2994e713ad63a58\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Orion\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"58882bfcd2994e0d53b1458a\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Groovy Green Hat\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"588852e8d2994e3bbfa52dad\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Darrell\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"587a951dd2994e15c7deaa00\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Azusa\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"lose\",\"opponent_id\":\"588852e8d2994e3bbfa52d8c\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Rocky\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"5877eb55d2994e15c7dea982\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"Darrell\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"win\",\"opponent_id\":\"587a951dd2994e15c7deaa00\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"},{\"opponent_name\":\"SPY | Nintendude\",\"tournament_name\":\"Norcal Validated 2\",\"result\":\"lose\",\"opponent_id\":\"588852e8d2994e3bbfa52da6\",\"tournament_id\":\"58a00514d2994e4d0f2e25a6\",\"tournament_date\":\"02/11/17\"}],\"player\":{\"id\":\"588852e7d2994e3bbfa52d6e\",\"name\":\"Laudandus\"},\"losses\":18,\"wins\":53}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        matchesBundle = gson.fromJson(JSON_MATCHES_BUNDLE, MatchesBundle::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithNullMatchesBundleAndNullMatchResult() {
        val presentation = playerToolbarManager.getPresentation(null, null)
        assertFalse(presentation.isFilterVisible)
        assertFalse(presentation.isFilterAllVisible)
        assertFalse(presentation.isFilterLossesVisible)
        assertFalse(presentation.isFilterWinsVisible)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithNullMatchesBundleAndLoseMatchResult() {
        val presentation = playerToolbarManager.getPresentation(null, MatchResult.LOSE)
        assertFalse(presentation.isFilterVisible)
        assertFalse(presentation.isFilterAllVisible)
        assertFalse(presentation.isFilterLossesVisible)
        assertFalse(presentation.isFilterWinsVisible)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithNullMatchesBundleAndWinMatchResult() {
        val presentation = playerToolbarManager.getPresentation(null, MatchResult.WIN)
        assertFalse(presentation.isFilterVisible)
        assertFalse(presentation.isFilterAllVisible)
        assertFalse(presentation.isFilterLossesVisible)
        assertFalse(presentation.isFilterWinsVisible)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithMatchesBundleAndNullMatchResult() {
        val presentation = playerToolbarManager.getPresentation(matchesBundle, null)
        assertTrue(presentation.isFilterVisible)
        assertFalse(presentation.isFilterAllVisible)
        assertTrue(presentation.isFilterLossesVisible)
        assertTrue(presentation.isFilterWinsVisible)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithMatchesBundleAndLoseMatchResult() {
        val presentation = playerToolbarManager.getPresentation(matchesBundle, MatchResult.LOSE)
        assertTrue(presentation.isFilterVisible)
        assertTrue(presentation.isFilterAllVisible)
        assertFalse(presentation.isFilterLossesVisible)
        assertTrue(presentation.isFilterWinsVisible)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithMatchesBundleAndWinMatchResult() {
        val presentation = playerToolbarManager.getPresentation(matchesBundle, MatchResult.WIN)
        assertTrue(presentation.isFilterVisible)
        assertTrue(presentation.isFilterAllVisible)
        assertTrue(presentation.isFilterLossesVisible)
        assertFalse(presentation.isFilterWinsVisible)
    }

}
