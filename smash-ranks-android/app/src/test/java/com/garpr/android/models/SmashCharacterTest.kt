package com.garpr.android.models

import com.garpr.android.BaseTest
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class SmashCharacterTest : BaseTest() {

    @Inject
    protected lateinit var mGson: Gson


    companion object {
        private const val JSON_BOWSER = "\"bow\""
        private const val JSON_CAPTAIN_FALCON = "\"fcn\""
        private const val JSON_DONKEY_KONG = "\"dnk\""
        private const val JSON_DR_MARIO = "\"doc\""
        private const val JSON_FALCO = "\"fco\""
        private const val JSON_FOX = "\"fox\""
        private const val JSON_ICE_CLIMBERS = "\"ice\""
        private const val JSON_JIGGLYPUFF = "\"puf\""
        private const val JSON_LINK = "\"lnk\""
        private const val JSON_LUIGI = "\"lgi\""
        private const val JSON_MARIO = "\"mar\""
        private const val JSON_MARTH = "\"mrt\""
        private const val JSON_MEWTWO = "\"mew\""
        private const val JSON_MR_GAME_AND_WATCH = "\"gnw\""
        private const val JSON_NESS = "\"nes\""
        private const val JSON_PEACH = "\"pch\""
        private const val JSON_PICHU = "\"pic\""
        private const val JSON_PIKACHU = "\"pik\""
        private const val JSON_ROY = "\"roy\""
        private const val JSON_SAMUS = "\"sam\""
        private const val JSON_SHEIK = "\"shk\""
        private const val JSON_YOSHI = "\"ysh\""
        private const val JSON_YOUNG_LINK = "\"ylk\""
        private const val JSON_ZELDA = "\"zld\""
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun testBowser() {
        assertEquals(JSON_BOWSER, mGson.toJson(SmashCharacter.BOWSER))
        assertEquals(SmashCharacter.BOWSER, mGson.fromJson(JSON_BOWSER, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testCaptainFalcon() {
        assertEquals(JSON_CAPTAIN_FALCON, mGson.toJson(SmashCharacter.CAPTAIN_FALCON))
        assertEquals(SmashCharacter.CAPTAIN_FALCON, mGson.fromJson(JSON_CAPTAIN_FALCON, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testDonkeyKong() {
        assertEquals(JSON_DONKEY_KONG, mGson.toJson(SmashCharacter.DONKEY_KONG))
        assertEquals(SmashCharacter.DONKEY_KONG, mGson.fromJson(JSON_DONKEY_KONG, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testDrMario() {
        assertEquals(JSON_DR_MARIO, mGson.toJson(SmashCharacter.DR_MARIO))
        assertEquals(SmashCharacter.DR_MARIO, mGson.fromJson(JSON_DR_MARIO, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testFalco() {
        assertEquals(JSON_FALCO, mGson.toJson(SmashCharacter.FALCO))
        assertEquals(SmashCharacter.FALCO, mGson.fromJson(JSON_FALCO, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testFox() {
        assertEquals(JSON_FOX, mGson.toJson(SmashCharacter.FOX))
        assertEquals(SmashCharacter.FOX, mGson.fromJson(JSON_FOX, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testIceClimbers() {
        assertEquals(JSON_ICE_CLIMBERS, mGson.toJson(SmashCharacter.ICE_CLIMBERS))
        assertEquals(SmashCharacter.ICE_CLIMBERS, mGson.fromJson(JSON_ICE_CLIMBERS, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testJigglypuff() {
        assertEquals(JSON_JIGGLYPUFF, mGson.toJson(SmashCharacter.JIGGLYPUFF))
        assertEquals(SmashCharacter.JIGGLYPUFF, mGson.fromJson(JSON_JIGGLYPUFF, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testKirby() {
        assertEquals(JSON_JIGGLYPUFF, mGson.toJson(SmashCharacter.JIGGLYPUFF))
        assertEquals(SmashCharacter.JIGGLYPUFF, mGson.fromJson(JSON_JIGGLYPUFF, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testLink() {
        assertEquals(JSON_LINK, mGson.toJson(SmashCharacter.LINK))
        assertEquals(SmashCharacter.LINK, mGson.fromJson(JSON_LINK, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testLuigi() {
        assertEquals(JSON_LUIGI, mGson.toJson(SmashCharacter.LUIGI))
        assertEquals(SmashCharacter.LUIGI, mGson.fromJson(JSON_LUIGI, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testMario() {
        assertEquals(JSON_MARIO, mGson.toJson(SmashCharacter.MARIO))
        assertEquals(SmashCharacter.MARIO, mGson.fromJson(JSON_MARIO, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testMarth() {
        assertEquals(JSON_MARTH, mGson.toJson(SmashCharacter.MARTH))
        assertEquals(SmashCharacter.MARTH, mGson.fromJson(JSON_MARTH, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testMewtwo() {
        assertEquals(JSON_MEWTWO, mGson.toJson(SmashCharacter.MEWTWO))
        assertEquals(SmashCharacter.MEWTWO, mGson.fromJson(JSON_MEWTWO, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testMrGameAndWatch() {
        assertEquals(JSON_MR_GAME_AND_WATCH, mGson.toJson(SmashCharacter.MR_GAME_AND_WATCH))
        assertEquals(SmashCharacter.MR_GAME_AND_WATCH, mGson.fromJson(JSON_MR_GAME_AND_WATCH, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testNess() {
        assertEquals(JSON_NESS, mGson.toJson(SmashCharacter.NESS))
        assertEquals(SmashCharacter.NESS, mGson.fromJson(JSON_NESS, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testPeach() {
        assertEquals(JSON_PEACH, mGson.toJson(SmashCharacter.PEACH))
        assertEquals(SmashCharacter.PEACH, mGson.fromJson(JSON_PEACH, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testPichu() {
        assertEquals(JSON_PICHU, mGson.toJson(SmashCharacter.PICHU))
        assertEquals(SmashCharacter.PICHU, mGson.fromJson(JSON_PICHU, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testPikachu() {
        assertEquals(JSON_PIKACHU, mGson.toJson(SmashCharacter.PIKACHU))
        assertEquals(SmashCharacter.PIKACHU, mGson.fromJson(JSON_PIKACHU, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testRoy() {
        assertEquals(JSON_ROY, mGson.toJson(SmashCharacter.ROY))
        assertEquals(SmashCharacter.ROY, mGson.fromJson(JSON_ROY, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testSamus() {
        assertEquals(JSON_SAMUS, mGson.toJson(SmashCharacter.SAMUS))
        assertEquals(SmashCharacter.SAMUS, mGson.fromJson(JSON_SAMUS, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testSheik() {
        assertEquals(JSON_SHEIK, mGson.toJson(SmashCharacter.SHEIK))
        assertEquals(SmashCharacter.SHEIK, mGson.fromJson(JSON_SHEIK, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testYoshi() {
        assertEquals(JSON_YOSHI, mGson.toJson(SmashCharacter.YOSHI))
        assertEquals(SmashCharacter.YOSHI, mGson.fromJson(JSON_YOSHI, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testYoungLink() {
        assertEquals(JSON_YOUNG_LINK, mGson.toJson(SmashCharacter.YOUNG_LINK))
        assertEquals(SmashCharacter.YOUNG_LINK, mGson.fromJson(JSON_YOUNG_LINK, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testZelda() {
        assertEquals(JSON_ZELDA, mGson.toJson(SmashCharacter.ZELDA))
        assertEquals(SmashCharacter.ZELDA, mGson.fromJson(JSON_ZELDA, SmashCharacter::class.java))
    }

}
