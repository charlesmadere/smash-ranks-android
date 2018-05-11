package com.garpr.android.networking

import com.garpr.android.BaseTest
import com.garpr.android.misc.Constants
import com.garpr.android.models.FullPlayer
import com.garpr.android.models.MatchesBundle
import com.garpr.android.models.PlayerMatchesBundle
import com.garpr.android.models.Region
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class PlayerMatchesBundleApiCallTest : BaseTest() {

    private lateinit var matchesSpark: MatchesBundle
    private lateinit var playerSpark: FullPlayer
    private lateinit var norcal: Region

    @Inject
    protected lateinit var gson: Gson


    companion object {
        private const val JSON_MATCHES_SPARK = "{\"matches\":[{\"opponent_name\":\"Zelxinoe\",\"tournament_name\":\"Mythic Mondays #9\",\"result\":\"win\",\"opponent_id\":\"58ad411dd2994e756952adcd\",\"tournament_id\":\"58ad40a1d2994e756952adc7\",\"tournament_date\":\"01/09/17\"},{\"opponent_name\":\"Easton\",\"tournament_name\":\"Mythic Mondays #9\",\"result\":\"win\",\"opponent_id\":\"588999c4d2994e713ad638ac\",\"tournament_id\":\"58ad40a1d2994e756952adc7\",\"tournament_date\":\"01/09/17\"},{\"opponent_name\":\"Rickety\",\"tournament_name\":\"Mythic Mondays #9\",\"result\":\"win\",\"opponent_id\":\"58ad411dd2994e756952adda\",\"tournament_id\":\"58ad40a1d2994e756952adc7\",\"tournament_date\":\"01/09/17\"},{\"opponent_name\":\"Rymo\",\"tournament_name\":\"Mythic Mondays #9\",\"result\":\"win\",\"opponent_id\":\"587a951dd2994e15c7dea9de\",\"tournament_id\":\"58ad40a1d2994e756952adc7\",\"tournament_date\":\"01/09/17\"},{\"opponent_name\":\"Rymo\",\"tournament_name\":\"Mythic Mondays #9\",\"result\":\"win\",\"opponent_id\":\"587a951dd2994e15c7dea9de\",\"tournament_id\":\"58ad40a1d2994e756952adc7\",\"tournament_date\":\"01/09/17\"},{\"opponent_name\":\"SlugNasty\",\"tournament_name\":\"Melee @ the Made 23\",\"result\":\"win\",\"opponent_id\":\"587a951dd2994e15c7dea9f6\",\"tournament_id\":\"5888282dd2994e0d53b14559\",\"tournament_date\":\"01/13/17\"},{\"opponent_name\":\"Charlezard\",\"tournament_name\":\"Melee @ the Made 23\",\"result\":\"win\",\"opponent_id\":\"587a951dd2994e15c7dea9fe\",\"tournament_id\":\"5888282dd2994e0d53b14559\",\"tournament_date\":\"01/13/17\"}],\"player\":{\"id\":\"5877eb55d2994e15c7dea97e\",\"name\":\"Spark\"},\"losses\":57,\"wins\":245}"
        private const val JSON_PLAYER_SPARK = "{\"ratings\":{\"norcal\":{\"mu\":43.53225705774309,\"sigma\":0.892201020269756}},\"name\":\"Spark\",\"regions\":[\"norcal\"],\"merge_parent\":null,\"merge_children\":[\"5877eb55d2994e15c7dea97e\"],\"id\":\"5877eb55d2994e15c7dea97e\",\"merged\":false,\"aliases\":[\"spark\"]}"
        private const val JSON_REGION_NORCAL = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":45,\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000,\"endpoint\":\"gar_pr\"}"
        private const val PLAYER_ID_SPARK = "5877eb55d2994e15c7dea97e"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        matchesSpark = gson.fromJson(JSON_MATCHES_SPARK, MatchesBundle::class.java)
        playerSpark = gson.fromJson(JSON_PLAYER_SPARK, FullPlayer::class.java)
        norcal = gson.fromJson(JSON_REGION_NORCAL, Region::class.java)
    }

    @Test
    fun testGetPlayerMatchesBundleWithNonNullMatchesNonNullPlayer() {
        var result: PlayerMatchesBundle? = null

        val listener = object : AbsApiListener<PlayerMatchesBundle>() {
            override fun success(`object`: PlayerMatchesBundle?) {
                result = `object`
            }
        }

        val serverApi = object : AbsServerApi() {
            override fun getMatches(region: Region, playerId: String,
                    listener: ApiListener<MatchesBundle>) {
                listener.success(matchesSpark)
            }

            override fun getPlayer(region: Region, playerId: String,
                    listener: ApiListener<FullPlayer>) {
                listener.success(playerSpark)
            }
        }

        PlayerMatchesBundleApiCall(listener, norcal, serverApi, PLAYER_ID_SPARK).fetch()
        assertNotNull(result)
        assertNotNull(result?.fullPlayer)
        assertNotNull(result?.matchesBundle)
    }

    @Test
    fun testGetPlayerMatchesBundleWithNonNullMatchesNullPlayer() {
        var result: Int? = null

        val listener = object : AbsApiListener<PlayerMatchesBundle>() {
            override fun failure(errorCode: Int) {
                result = errorCode
            }
        }

        val serverApi = object : AbsServerApi() {
            override fun getMatches(region: Region, playerId: String,
                    listener: ApiListener<MatchesBundle>) {
                listener.success(matchesSpark)
            }

            override fun getPlayer(region: Region, playerId: String,
                    listener: ApiListener<FullPlayer>) {
                listener.success(null)
            }
        }

        PlayerMatchesBundleApiCall(listener, norcal, serverApi, PLAYER_ID_SPARK).fetch()
        assertNotNull(result)
        assertEquals(Constants.ERROR_CODE_UNKNOWN, result)
    }

    @Test
    fun testGetPlayerMatchesBundleWithNullMatchesNonNullPlayer() {
        var result: PlayerMatchesBundle? = null

        val listener = object : AbsApiListener<PlayerMatchesBundle>() {
            override fun success(`object`: PlayerMatchesBundle?) {
                result = `object`
            }
        }

        val serverApi = object : AbsServerApi() {
            override fun getMatches(region: Region, playerId: String,
                    listener: ApiListener<MatchesBundle>) {
                listener.success(null)
            }

            override fun getPlayer(region: Region, playerId: String,
                    listener: ApiListener<FullPlayer>) {
                listener.success(playerSpark)
            }
        }

        PlayerMatchesBundleApiCall(listener, norcal, serverApi, PLAYER_ID_SPARK).fetch()
        assertNotNull(result)
        assertNotNull(result?.fullPlayer)
        assertNull(result?.matchesBundle)
    }

    @Test
    fun testGetPlayerMatchesBundleWithNullMatchesNullPlayer() {
        var result: Int? = null

        val listener = object : AbsApiListener<PlayerMatchesBundle>() {
            override fun failure(errorCode: Int) {
                result = errorCode
            }
        }

        val serverApi = object : AbsServerApi() {
            override fun getMatches(region: Region, playerId: String,
                    listener: ApiListener<MatchesBundle>) {
                listener.success(null)
            }

            override fun getPlayer(region: Region, playerId: String,
                    listener: ApiListener<FullPlayer>) {
                listener.success(null)
            }
        }

        PlayerMatchesBundleApiCall(listener, norcal, serverApi, PLAYER_ID_SPARK).fetch()
        assertNotNull(result)
        assertEquals(Constants.ERROR_CODE_UNKNOWN, result)
    }

}
