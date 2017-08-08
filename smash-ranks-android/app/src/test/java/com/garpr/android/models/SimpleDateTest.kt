package com.garpr.android.models

import com.garpr.android.BaseTest
import com.garpr.android.BuildConfig
import com.google.gson.Gson
import com.google.gson.JsonElement
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class SimpleDateTest : BaseTest() {

    @Inject
    lateinit protected var mGson: Gson


    companion object {
        private const val JSON_ONE = "\"01/05/17\""
        private const val JSON_TWO = "\"11/28/89\""
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun testChronologicalOrder() {
        val list = mutableListOf(SimpleDate(Date(2)), SimpleDate(Date(0)), SimpleDate(Date(1)),
                SimpleDate(Date(5)), SimpleDate(Date(20)))

        Collections.sort(list, SimpleDate.CHRONOLOGICAL_ORDER)
        assertEquals(0, list[0].date.time)
        assertEquals(1, list[1].date.time)
        assertEquals(2, list[2].date.time)
        assertEquals(5, list[3].date.time)
        assertEquals(20, list[4].date.time)
    }

    @Test
    @Throws(Exception::class)
    fun testFromEmptyString() {
        val simpleDate = mGson.fromJson("", SimpleDate::class.java)
        assertNull(simpleDate)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJson() {
        var simpleDate = mGson.fromJson(JSON_ONE, SimpleDate::class.java)
        assertNotNull(simpleDate)

        simpleDate = mGson.fromJson(JSON_TWO, SimpleDate::class.java)
        assertNotNull(simpleDate)
    }

    @Test
    @Throws(Exception::class)
    fun testFromNullJsonElement() {
        val simpleDate = mGson.fromJson(null as JsonElement?, SimpleDate::class.java)
        assertNull(simpleDate)
    }

    @Test
    @Throws(Exception::class)
    fun testFromNullString() {
        val simpleDate = mGson.fromJson(null as String?, SimpleDate::class.java)
        assertNull(simpleDate)
    }

    @Test
    @Throws(Exception::class)
    fun testHappenedAfter() {
        val simpleDate1 = mGson.fromJson(JSON_ONE, SimpleDate::class.java)
        val simpleDate2 = mGson.fromJson(JSON_TWO, SimpleDate::class.java)
        assertTrue(simpleDate1.happenedAfter(simpleDate2))
        assertFalse(simpleDate2.happenedAfter(simpleDate1))
    }

    @Test
    @Throws(Exception::class)
    fun testReverseChronologicalOrder() {
        val list = mutableListOf(SimpleDate(Date(2)), SimpleDate(Date(0)), SimpleDate(Date(1)),
                SimpleDate(Date(5)), SimpleDate(Date(20)))

        Collections.sort(list, SimpleDate.REVERSE_CHRONOLOGICAL_ORDER)
        assertEquals(20, list[0].date.time)
        assertEquals(5, list[1].date.time)
        assertEquals(2, list[2].date.time)
        assertEquals(1, list[3].date.time)
        assertEquals(0, list[4].date.time)
    }

    @Test
    @Throws(Exception::class)
    fun testToJson() {
        var simpleDate1 = mGson.fromJson(JSON_ONE, SimpleDate::class.java)
        var json = mGson.toJson(simpleDate1, SimpleDate::class.java)
        var simpleDate2 = mGson.fromJson(json, SimpleDate::class.java)
        assertEquals(simpleDate1, simpleDate2)

        simpleDate1 = mGson.fromJson(JSON_TWO, SimpleDate::class.java)
        json = mGson.toJson(simpleDate1, SimpleDate::class.java)
        simpleDate2 = mGson.fromJson(json, SimpleDate::class.java)
        assertEquals(simpleDate1, simpleDate2)
    }

    @Test
    @Throws(Exception::class)
    fun testValuesWithJsonOne() {
        val simpleDate = mGson.fromJson(JSON_ONE, SimpleDate::class.java)
        val calendar = Calendar.getInstance()
        calendar.time = simpleDate.date

        assertEquals(2017, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH))
        assertEquals(5, calendar.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    @Throws(Exception::class)
    fun testValuesWithJsonTwo() {
        val simpleDate = mGson.fromJson(JSON_TWO, SimpleDate::class.java)
        val calendar = Calendar.getInstance()
        calendar.time = simpleDate.date

        assertEquals(1989, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.NOVEMBER, calendar.get(Calendar.MONTH))
        assertEquals(28, calendar.get(Calendar.DAY_OF_MONTH))
    }

}
