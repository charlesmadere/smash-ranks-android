package com.garpr.android.misc

import com.garpr.android.BaseTest
import com.garpr.android.models.FullTournament
import com.google.gson.Gson
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class TournamentToolbarManagerTest : BaseTest() {

    lateinit private var tournamentWithUrl: FullTournament
    lateinit private var tournamentWithoutUrl: FullTournament

    @Inject
    lateinit protected var gson: Gson

    @Inject
    lateinit protected var tournamentToolbarManager: TournamentToolbarManager


    companion object {
        private const val JSON_TOURNAMENT_WITH_URL = "{\"name\":\"Melee @ the Made 23\",\"players\":[{\"id\":\"587a951dd2994e15c7deaa01\",\"name\":\"Thomdore\"},{\"id\":\"587a951dd2994e15c7dea9eb\",\"name\":\"DarkSilence\"}],\"url\":\"http://challonge.com/MADE23Singless\",\"regions\":[\"norcal\"],\"matches\":[{\"loser_name\":\"DarkSilence\",\"match_id\":1,\"winner_id\":\"587a951dd2994e15c7dea9fd\",\"winner_name\":\"CarrierPig\",\"loser_id\":\"587a951dd2994e15c7dea9eb\",\"excluded\":false},{\"loser_name\":\"Thomdore\",\"match_id\":3,\"winner_id\":\"587a951dd2994e15c7dea9f9\",\"winner_name\":\"Untitled\",\"loser_id\":\"587a951dd2994e15c7deaa01\",\"excluded\":false}],\"raw_id\":\"5888282dd2994e0d53b14558\",\"date\":\"01/13/17\",\"type\":\"challonge\",\"id\":\"5888282dd2994e0d53b14559\"}"
        private const val JSON_TOURNAMENT_WITHOUT_URL = "{\"name\":\"Norcal Validated 3\",\"players\":[{\"id\":\"587894e9d2994e15c7dea9cd\",\"name\":\"Darkatma\"},{\"id\":\"5877eb55d2994e15c7dea97e\",\"name\":\"Spark\"}],\"regions\":[\"norcal\"],\"matches\":[{\"loser_name\":\"Darkatma\",\"match_id\":196,\"winner_id\":\"5877eb55d2994e15c7dea97e\",\"winner_name\":\"Spark\",\"loser_id\":\"587894e9d2994e15c7dea9cd\",\"excluded\":false},{\"loser_name\":\"Spark\",\"match_id\":197,\"winner_id\":\"588852e8d2994e3bbfa52d8c\",\"winner_name\":\"Azusa\",\"loser_id\":\"5877eb55d2994e15c7dea97e\",\"excluded\":false}],\"raw_id\":\"58d8c3e8d2994e057e91f7fc\",\"date\":\"03/25/17\",\"type\":\"smashgg\",\"id\":\"58d8c3e8d2994e057e91f7fd\"}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        tournamentWithUrl = gson.fromJson(JSON_TOURNAMENT_WITH_URL, FullTournament::class.java)
        tournamentWithoutUrl = gson.fromJson(JSON_TOURNAMENT_WITHOUT_URL, FullTournament::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithNull() {
        val presentation = tournamentToolbarManager.getPresentation(null)
        assertFalse(presentation.isShareVisible)
        assertFalse(presentation.isViewTournamentPageVisible)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithTournamentWithUrl() {
        val presentation = tournamentToolbarManager.getPresentation(tournamentWithUrl)
        assertTrue(presentation.isShareVisible)
        assertTrue(presentation.isViewTournamentPageVisible)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithTournamentWithoutUrl() {
        val presentation = tournamentToolbarManager.getPresentation(tournamentWithoutUrl)
        assertTrue(presentation.isShareVisible)
        assertFalse(presentation.isViewTournamentPageVisible)
    }

}
