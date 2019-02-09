package com.garpr.android.data.models

import com.garpr.android.BaseTest
import com.garpr.android.extensions.requireFromJson
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Collections
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class AbsPlayerTest : BaseTest() {

    private lateinit var favoritePlayer1: AbsPlayer
    private lateinit var favoritePlayer2: AbsPlayer
    private lateinit var fullPlayer1: AbsPlayer
    private lateinit var litePlayer1: AbsPlayer
    private lateinit var litePlayer2: AbsPlayer
    private lateinit var rankedPlayer1: AbsPlayer
    private lateinit var rankedPlayer2: AbsPlayer

    @Inject
    protected lateinit var moshi: Moshi


    companion object {
        private const val JSON_FAVORITE_PLAYER_1 = "{\"region\":{\"endpoint\":\"gar_pr\",\"display_name\":\"Norcal\",\"id\":\"norcal\"},\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"mikkuz\"}"
        private const val JSON_FAVORITE_PLAYER_2 = "{\"region\":{\"endpoint\":\"not_gar_pr\",\"display_name\":\"Georgia\",\"id\":\"georgia\"},\"id\":\"583a4a15d2994e0577b05c86\",\"name\":\"druggedfox\"}"
        private const val JSON_FULL_PLAYER_1 = "{\"ratings\":{\"googlemtv\":{\"mu\":37.05546025182014,\"sigma\":2.0824461049194727},\"norcal\":{\"mu\":37.02140742867105,\"sigma\":2.3075802611877165}},\"name\":\"gaR\",\"regions\":[\"norcal\",\"googlemtv\"],\"merge_children\":[\"58523b44d2994e15c7dea945\"],\"id\":\"58523b44d2994e15c7dea945\",\"merged\":false,\"merge_parent\":null}"
        private const val JSON_LITE_PLAYER_1 = "{\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"homemadewaffles\"}"
        private const val JSON_LITE_PLAYER_2 = "{\"id\":\"5877eb55d2994e15c7dea97e\",\"name\":\"Spark\"}"
        private const val JSON_RANKED_PLAYER_1 = "{\"rating\":36.81988474543549,\"name\":\"ycz6\",\"rank\":13,\"previous_rank\":16,\"id\":\"5888542ad2994e3bbfa52de4\"}"
        private const val JSON_RANKED_PLAYER_2 = "{\"id\":\"53c64dba8ab65f6e6651f7bc\",\"name\":\"Hax\",\"rank\":2,\"rating\":37.975921649503086}"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        val absPlayerAdapter = moshi.adapter(AbsPlayer::class.java)
        favoritePlayer1 = absPlayerAdapter.requireFromJson(JSON_FAVORITE_PLAYER_1)
        favoritePlayer2 = absPlayerAdapter.requireFromJson(JSON_FAVORITE_PLAYER_2)
        fullPlayer1 = absPlayerAdapter.requireFromJson(JSON_FULL_PLAYER_1)
        litePlayer1 = absPlayerAdapter.requireFromJson(JSON_LITE_PLAYER_1)
        litePlayer2 = absPlayerAdapter.requireFromJson(JSON_LITE_PLAYER_2)
        rankedPlayer1 = absPlayerAdapter.requireFromJson(JSON_RANKED_PLAYER_1)
        rankedPlayer2 = absPlayerAdapter.requireFromJson(JSON_RANKED_PLAYER_2)
    }

    @Test
    fun testComparatorAlphabeticalOrder() {
        val list = listOf(fullPlayer1, litePlayer2, litePlayer1, favoritePlayer2, favoritePlayer1)
        Collections.sort(list, AbsPlayer.ALPHABETICAL_ORDER)

        assertEquals(favoritePlayer2, list[0])
        assertEquals(fullPlayer1, list[1])
        assertEquals(litePlayer1, list[2])
        assertEquals(favoritePlayer1, list[3])
        assertEquals(litePlayer2, list[4])
    }

    @Test
    fun testEquals() {
        assertEquals(favoritePlayer1, favoritePlayer1)
        assertEquals(favoritePlayer2, favoritePlayer2)
        assertNotEquals(favoritePlayer1, favoritePlayer2)

        assertEquals(fullPlayer1, fullPlayer1)
        assertNotEquals(fullPlayer1, litePlayer1)
        assertNotEquals(fullPlayer1, rankedPlayer1)

        assertEquals(litePlayer1, litePlayer1)
        assertEquals(litePlayer2, litePlayer2)
        assertNotEquals(litePlayer1, litePlayer2)

        assertEquals(rankedPlayer1, rankedPlayer1)
        assertEquals(rankedPlayer2, rankedPlayer2)
        assertNotEquals(rankedPlayer1, rankedPlayer2)
    }

    @Test
    fun testHashCode() {
        assertEquals("583a4a15d2994e0577b05c74".hashCode(), favoritePlayer1.hashCode())
        assertEquals("583a4a15d2994e0577b05c86".hashCode(), favoritePlayer2.hashCode())
        assertEquals("58523b44d2994e15c7dea945".hashCode(), fullPlayer1.hashCode())
        assertEquals("583a4a15d2994e0577b05c74".hashCode(), litePlayer1.hashCode())
        assertEquals("5877eb55d2994e15c7dea97e".hashCode(), litePlayer2.hashCode())
        assertEquals("5888542ad2994e3bbfa52de4".hashCode(), rankedPlayer1.hashCode())
        assertEquals("53c64dba8ab65f6e6651f7bc".hashCode(), rankedPlayer2.hashCode())
    }

}
