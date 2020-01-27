package com.garpr.android.data.database

import com.garpr.android.test.BaseTest
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class MapOfStringToStringConverterTest : BaseTest() {

    private lateinit var jsonAdapter: JsonAdapter<Map<String, String>>
    private val converter = MapOfStringToStringConverter()

    protected val moshi: Moshi by inject()

    @Before
    override fun setUp() {
        super.setUp()

        jsonAdapter = moshi.adapter(Types.newParameterizedType(Map::class.java,
                String::class.java, String::class.java))
    }

    @Test
    fun testMapOfStringToStringFromString() {
        val map = converter.mapOfStringToStringFromString(JSON)
        assertNotNull(map)
        assertEquals(1, map?.size)
        assertEquals("World", map?.get("Hello"))
    }

    @Test
    fun testMapOfStringToStringFromStringWithEmptyString() {
        assertNull(converter.mapOfStringToStringFromString(""))
    }

    @Test
    fun testMapOfStringToStringFromStringWithNull() {
        assertNull(converter.mapOfStringToStringFromString(null))
    }

    @Test
    fun testStringFromMapOfStringToString() {
        val string = converter.stringFromMapOfStringToString(MAP)
        assertFalse(string.isNullOrBlank())

        val map = jsonAdapter.fromJson(string!!)
        assertEquals(1, map?.size)
        assertEquals("World", map?.get("Hello"))
    }

    @Test
    fun testStringFromMapOfStringToStringWithEmptyMap() {
        assertNull(converter.stringFromMapOfStringToString(emptyMap()))
    }

    @Test
    fun testStringFromMapOfStringToStringWithNull() {
        assertNull(converter.stringFromMapOfStringToString(null))
    }

    companion object {
        private val MAP = mapOf(
                "Hello" to "World"
        )

        private const val JSON = "{\"Hello\":\"World\"}"
    }

}
