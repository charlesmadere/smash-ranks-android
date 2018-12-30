package com.garpr.android.models

import android.os.Parcel
import com.garpr.android.BaseTest
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Calendar
import java.util.Collections
import java.util.Date
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class SimpleDateTest : BaseTest() {

    @Inject
    protected lateinit var gson: Gson


    companion object {
        private const val JSON_ONE = "\"01/05/17\""
        private const val JSON_TWO = "\"11/28/89\""
        private val LONG_ONE = JsonPrimitive(0)
        private val LONG_TWO = JsonPrimitive(1505859302322)
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    fun testChronologicalOrder() {
        val list = listOf(SimpleDate(Date(2)), SimpleDate(Date(0)),
                SimpleDate(Date(1)), SimpleDate(Date(5)), SimpleDate(Date(20)))

        Collections.sort(list, SimpleDate.CHRONOLOGICAL_ORDER)
        assertEquals(0, list[0].date.time)
        assertEquals(1, list[1].date.time)
        assertEquals(2, list[2].date.time)
        assertEquals(5, list[3].date.time)
        assertEquals(20, list[4].date.time)
    }

    @Test
    fun testFromBlankString() {
        val simpleDate = gson.fromJson(" ", SimpleDate::class.java)
        assertNull(simpleDate)
    }

    @Test
    fun testFromEmptyString() {
        val simpleDate = gson.fromJson("", SimpleDate::class.java)
        assertNull(simpleDate)
    }

    @Test
    fun testFromJson() {
        var simpleDate = gson.fromJson(JSON_ONE, SimpleDate::class.java)
        assertNotNull(simpleDate)

        simpleDate = gson.fromJson(JSON_TWO, SimpleDate::class.java)
        assertNotNull(simpleDate)
    }

    @Test
    fun testFromLong() {
        var simpleDate = gson.fromJson(LONG_ONE, SimpleDate::class.java)
        assertNotNull(simpleDate)
        assertEquals(0, simpleDate.date.time)

        simpleDate = gson.fromJson(LONG_TWO, SimpleDate::class.java)
        assertNotNull(simpleDate)
    }

    @Test
    fun testFromNullJsonElement() {
        val simpleDate = gson.fromJson(null as JsonElement?, SimpleDate::class.java)
        assertNull(simpleDate)
    }

    @Test
    fun testFromNullString() {
        val simpleDate = gson.fromJson(null as String?, SimpleDate::class.java)
        assertNull(simpleDate)
    }

    @Test
    fun testHappenedAfter() {
        val simpleDate1 = gson.fromJson(JSON_ONE, SimpleDate::class.java)
        val simpleDate2 = gson.fromJson(JSON_TWO, SimpleDate::class.java)
        val simpleDate3 = gson.fromJson(LONG_ONE, SimpleDate::class.java)
        val simpleDate4 = gson.fromJson(LONG_TWO, SimpleDate::class.java)

        assertFalse(simpleDate1.happenedAfter(simpleDate1))
        assertTrue(simpleDate1.happenedAfter(simpleDate2))
        assertTrue(simpleDate1.happenedAfter(simpleDate3))
        assertFalse(simpleDate1.happenedAfter(simpleDate4))

        assertFalse(simpleDate2.happenedAfter(simpleDate1))
        assertFalse(simpleDate2.happenedAfter(simpleDate2))
        assertTrue(simpleDate2.happenedAfter(simpleDate3))
        assertFalse(simpleDate2.happenedAfter(simpleDate4))

        assertFalse(simpleDate3.happenedAfter(simpleDate1))
        assertFalse(simpleDate3.happenedAfter(simpleDate2))
        assertFalse(simpleDate3.happenedAfter(simpleDate3))
        assertFalse(simpleDate3.happenedAfter(simpleDate4))

        assertTrue(simpleDate4.happenedAfter(simpleDate1))
        assertTrue(simpleDate4.happenedAfter(simpleDate2))
        assertTrue(simpleDate4.happenedAfter(simpleDate3))
        assertFalse(simpleDate4.happenedAfter(simpleDate4))
    }

    @Test
    fun testParcelable() {
        val simpleDate = SimpleDate()
        val parcel = Parcel.obtain()
        simpleDate.writeToParcel(parcel, simpleDate.describeContents())
        parcel.setDataPosition(0)

        val simpleDateFromParcel = SimpleDate.CREATOR.createFromParcel(parcel)
        assertEquals(simpleDate, simpleDateFromParcel)
    }

    @Test
    fun testReverseChronologicalOrder() {
        val list = listOf(SimpleDate(Date(2)), SimpleDate(Date(0)), SimpleDate(Date(1)),
                SimpleDate(Date(5)), SimpleDate(Date(20)))

        Collections.sort(list, SimpleDate.REVERSE_CHRONOLOGICAL_ORDER)
        assertEquals(20, list[0].date.time)
        assertEquals(5, list[1].date.time)
        assertEquals(2, list[2].date.time)
        assertEquals(1, list[3].date.time)
        assertEquals(0, list[4].date.time)
    }

    @Test
    fun testToJson() {
        var simpleDate1 = gson.fromJson(JSON_ONE, SimpleDate::class.java)
        var json = gson.toJson(simpleDate1, SimpleDate::class.java)
        var simpleDate2 = gson.fromJson(json, SimpleDate::class.java)
        assertEquals(simpleDate1, simpleDate2)

        simpleDate1 = gson.fromJson(JSON_TWO, SimpleDate::class.java)
        json = gson.toJson(simpleDate1, SimpleDate::class.java)
        simpleDate2 = gson.fromJson(json, SimpleDate::class.java)
        assertEquals(simpleDate1, simpleDate2)
    }

    @Test
    fun testValuesWithJsonOne() {
        val simpleDate = gson.fromJson(JSON_ONE, SimpleDate::class.java)
        val calendar = Calendar.getInstance()
        calendar.time = simpleDate.date

        assertEquals(2017, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH))
        assertEquals(5, calendar.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun testValuesWithJsonTwo() {
        val simpleDate = gson.fromJson(JSON_TWO, SimpleDate::class.java)
        val calendar = Calendar.getInstance()
        calendar.time = simpleDate.date

        assertEquals(1989, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.NOVEMBER, calendar.get(Calendar.MONTH))
        assertEquals(28, calendar.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun testValuesWithLongOne() {
        val simpleDate = gson.fromJson(LONG_ONE, SimpleDate::class.java)
        assertEquals(0, simpleDate.date.time)

        val calendar = Calendar.getInstance()
        calendar.time = simpleDate.date

        assertEquals(1969, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH))
        assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun testValuesWithLongTwo() {
        val simpleDate = gson.fromJson(LONG_TWO, SimpleDate::class.java)
        assertEquals(1505859302322, simpleDate.date.time)

        val calendar = Calendar.getInstance()
        calendar.time = simpleDate.date

        assertEquals(2017, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.SEPTEMBER, calendar.get(Calendar.MONTH))
        assertEquals(19, calendar.get(Calendar.DAY_OF_MONTH))
    }

}
