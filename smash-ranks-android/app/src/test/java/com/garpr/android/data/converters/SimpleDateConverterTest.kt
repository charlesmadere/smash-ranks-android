package com.garpr.android.data.converters

import com.garpr.android.BaseTest
import com.garpr.android.data.models.SimpleDate
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Date
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class SimpleDateConverterTest : BaseTest() {

    @Inject
    protected lateinit var moshi: Moshi

    private lateinit var simpleDateAdapter: JsonAdapter<SimpleDate>


    companion object {
        private const val JSON_NUMBER_0 = "0"
        private const val JSON_NUMBER_1 = "1505859302322"
        private const val JSON_STRING_0 = "\"01/05/17\""
        private const val JSON_STRING_1 = "\"11/28/89\""

        private val SIMPLE_DATE_0 = SimpleDate(Date(0L))
        private val SIMPLE_DATE_1 = SimpleDate(Date(1505859302322L))
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        simpleDateAdapter = moshi.adapter(SimpleDate::class.java)
    }

    @Test
    fun testFromJsonWithNumber0() {
        val simpleDate = simpleDateAdapter.fromJson(JSON_NUMBER_0)
        assertEquals(0L, simpleDate?.date?.time)
    }

    @Test
    fun testFromJsonWithNumber1() {
        val simpleDate = simpleDateAdapter.fromJson(JSON_NUMBER_1)
        assertEquals(1505859302322L, simpleDate?.date?.time)
    }

    @Test
    fun testFromJsonWithString0() {
        val simpleDate = simpleDateAdapter.fromJson(JSON_STRING_0)
        assertEquals(1483603200000L, simpleDate?.date?.time)
    }

    @Test
    fun testFromJsonWithString1() {
        val simpleDate = simpleDateAdapter.fromJson(JSON_STRING_1)
        assertEquals(628243200000L, simpleDate?.date?.time)
    }

    @Test
    fun testToJsonWithNull() {
        val json = simpleDateAdapter.toJson(null)
        assertTrue(json.isNullOrEmpty())
    }

    @Test
    fun testToJsonWithSimpleDate0() {
        val json = simpleDateAdapter.toJson(SIMPLE_DATE_0)
        assertFalse(json.isNullOrBlank())

        val simpleDate = simpleDateAdapter.fromJson(json)
        assertEquals(SIMPLE_DATE_0, simpleDate)
    }

    @Test
    fun testToJsonWithSimpleDate1() {
        val json = simpleDateAdapter.toJson(SIMPLE_DATE_1)
        assertFalse(json.isNullOrBlank())

        val simpleDate = simpleDateAdapter.fromJson(json)
        assertEquals(SIMPLE_DATE_1, simpleDate)
    }

}