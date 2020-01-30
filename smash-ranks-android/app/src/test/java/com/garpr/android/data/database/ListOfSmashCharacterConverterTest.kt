package com.garpr.android.data.database

import com.garpr.android.data.models.SmashCharacter
import com.garpr.android.test.BaseTest
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class ListOfSmashCharacterConverterTest : BaseTest() {

    private lateinit var jsonAdapter: JsonAdapter<List<SmashCharacter?>?>
    private val converter = ListOfSmashCharacterConverter()

    protected val moshi: Moshi by inject()

    @Before
    override fun setUp() {
        super.setUp()

        jsonAdapter = moshi.adapter(Types.newParameterizedType(List::class.java,
                SmashCharacter::class.java))
    }

    @Test
    fun testListOfSmashCharacterFromStringWithDualMain() {
        val list = converter.listOfSmashCharacterFromString(DUAL_MAIN_JSON)
        assertEquals(2, list?.size)
        assertEquals(true, list?.contains(SmashCharacter.ICE_CLIMBERS))
        assertEquals(true, list?.contains(SmashCharacter.JIGGLYPUFF))
    }

    @Test
    fun testListOfSmashCharacterFromStringWithEmptyString() {
        assertNull(converter.listOfSmashCharacterFromString(""))
    }

    @Test
    fun testListOfSmashCharacterFromStringWithNull() {
        assertNull(converter.listOfSmashCharacterFromString(null))
    }

    @Test
    fun testListOfSmashCharacterFromStringWithSoloMain() {
        val list = converter.listOfSmashCharacterFromString(SOLO_MAIN_JSON)
        assertEquals(1, list?.size)
        assertEquals(true, list?.contains(SmashCharacter.CPTN_FALCON))
    }

    @Test
    fun testStringFromListOfSmashCharacterWithDualMain() {
        val string = converter.stringFromListOfSmashCharacter(DUAL_MAIN)
        assertFalse(string.isNullOrBlank())

        val list = jsonAdapter.fromJson(string!!)
        assertEquals(2, list?.size)
        assertEquals(true, list?.contains(SmashCharacter.FALCO))
        assertEquals(true, list?.contains(SmashCharacter.FOX))
    }

    @Test
    fun testStringFromListOfSmashCharacterWithEmptyList() {
        assertNull(converter.stringFromListOfSmashCharacter(emptyList()))
    }

    @Test
    fun testStringFromListOfSmashCharacterWithNull() {
        assertNull(converter.stringFromListOfSmashCharacter(null))
    }

    @Test
    fun testStringFromListOfSmashCharacterWithSoloMain() {
        val string = converter.stringFromListOfSmashCharacter(SOLO_MAIN)
        assertFalse(string.isNullOrBlank())

        val list = jsonAdapter.fromJson(string!!)
        assertEquals(1, list?.size)
        assertEquals(true, list?.contains(SmashCharacter.SHEIK))
    }

    companion object {
        private val DUAL_MAIN = listOf(SmashCharacter.FOX, SmashCharacter.FALCO)
        private val SOLO_MAIN = listOf(SmashCharacter.SHEIK)

        private const val DUAL_MAIN_JSON = "[\"ics\",\"puf\"]"
        private const val SOLO_MAIN_JSON = "[\"fcn\"]"
    }

}
