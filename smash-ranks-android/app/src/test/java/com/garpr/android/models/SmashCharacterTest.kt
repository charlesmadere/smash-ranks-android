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
    protected lateinit var gson: Gson


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
        assertEquals(JSON_BOWSER, gson.toJson(SmashCharacter.BOWSER))
        assertEquals(SmashCharacter.BOWSER, gson.fromJson(JSON_BOWSER, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testCaptainFalcon() {
        assertEquals(JSON_CAPTAIN_FALCON, gson.toJson(SmashCharacter.CAPTAIN_FALCON))
        assertEquals(SmashCharacter.CAPTAIN_FALCON, gson.fromJson(JSON_CAPTAIN_FALCON, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testDonkeyKong() {
        assertEquals(JSON_DONKEY_KONG, gson.toJson(SmashCharacter.DONKEY_KONG))
        assertEquals(SmashCharacter.DONKEY_KONG, gson.fromJson(JSON_DONKEY_KONG, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testDrMario() {
        assertEquals(JSON_DR_MARIO, gson.toJson(SmashCharacter.DR_MARIO))
        assertEquals(SmashCharacter.DR_MARIO, gson.fromJson(JSON_DR_MARIO, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testFalco() {
        assertEquals(JSON_FALCO, gson.toJson(SmashCharacter.FALCO))
        assertEquals(SmashCharacter.FALCO, gson.fromJson(JSON_FALCO, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testFox() {
        assertEquals(JSON_FOX, gson.toJson(SmashCharacter.FOX))
        assertEquals(SmashCharacter.FOX, gson.fromJson(JSON_FOX, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testIceClimbers() {
        assertEquals(JSON_ICE_CLIMBERS, gson.toJson(SmashCharacter.ICE_CLIMBERS))
        assertEquals(SmashCharacter.ICE_CLIMBERS, gson.fromJson(JSON_ICE_CLIMBERS, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testJigglypuff() {
        assertEquals(JSON_JIGGLYPUFF, gson.toJson(SmashCharacter.JIGGLYPUFF))
        assertEquals(SmashCharacter.JIGGLYPUFF, gson.fromJson(JSON_JIGGLYPUFF, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testKirby() {
        assertEquals(JSON_JIGGLYPUFF, gson.toJson(SmashCharacter.JIGGLYPUFF))
        assertEquals(SmashCharacter.JIGGLYPUFF, gson.fromJson(JSON_JIGGLYPUFF, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testLink() {
        assertEquals(JSON_LINK, gson.toJson(SmashCharacter.LINK))
        assertEquals(SmashCharacter.LINK, gson.fromJson(JSON_LINK, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testLuigi() {
        assertEquals(JSON_LUIGI, gson.toJson(SmashCharacter.LUIGI))
        assertEquals(SmashCharacter.LUIGI, gson.fromJson(JSON_LUIGI, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testMario() {
        assertEquals(JSON_MARIO, gson.toJson(SmashCharacter.MARIO))
        assertEquals(SmashCharacter.MARIO, gson.fromJson(JSON_MARIO, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testMarth() {
        assertEquals(JSON_MARTH, gson.toJson(SmashCharacter.MARTH))
        assertEquals(SmashCharacter.MARTH, gson.fromJson(JSON_MARTH, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testMewtwo() {
        assertEquals(JSON_MEWTWO, gson.toJson(SmashCharacter.MEWTWO))
        assertEquals(SmashCharacter.MEWTWO, gson.fromJson(JSON_MEWTWO, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testMrGameAndWatch() {
        assertEquals(JSON_MR_GAME_AND_WATCH, gson.toJson(SmashCharacter.MR_GAME_AND_WATCH))
        assertEquals(SmashCharacter.MR_GAME_AND_WATCH, gson.fromJson(JSON_MR_GAME_AND_WATCH, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testNess() {
        assertEquals(JSON_NESS, gson.toJson(SmashCharacter.NESS))
        assertEquals(SmashCharacter.NESS, gson.fromJson(JSON_NESS, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testPeach() {
        assertEquals(JSON_PEACH, gson.toJson(SmashCharacter.PEACH))
        assertEquals(SmashCharacter.PEACH, gson.fromJson(JSON_PEACH, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testPichu() {
        assertEquals(JSON_PICHU, gson.toJson(SmashCharacter.PICHU))
        assertEquals(SmashCharacter.PICHU, gson.fromJson(JSON_PICHU, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testPikachu() {
        assertEquals(JSON_PIKACHU, gson.toJson(SmashCharacter.PIKACHU))
        assertEquals(SmashCharacter.PIKACHU, gson.fromJson(JSON_PIKACHU, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testRoy() {
        assertEquals(JSON_ROY, gson.toJson(SmashCharacter.ROY))
        assertEquals(SmashCharacter.ROY, gson.fromJson(JSON_ROY, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testSamus() {
        assertEquals(JSON_SAMUS, gson.toJson(SmashCharacter.SAMUS))
        assertEquals(SmashCharacter.SAMUS, gson.fromJson(JSON_SAMUS, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testSheik() {
        assertEquals(JSON_SHEIK, gson.toJson(SmashCharacter.SHEIK))
        assertEquals(SmashCharacter.SHEIK, gson.fromJson(JSON_SHEIK, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testYoshi() {
        assertEquals(JSON_YOSHI, gson.toJson(SmashCharacter.YOSHI))
        assertEquals(SmashCharacter.YOSHI, gson.fromJson(JSON_YOSHI, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testYoungLink() {
        assertEquals(JSON_YOUNG_LINK, gson.toJson(SmashCharacter.YOUNG_LINK))
        assertEquals(SmashCharacter.YOUNG_LINK, gson.fromJson(JSON_YOUNG_LINK, SmashCharacter::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testZelda() {
        assertEquals(JSON_ZELDA, gson.toJson(SmashCharacter.ZELDA))
        assertEquals(SmashCharacter.ZELDA, gson.fromJson(JSON_ZELDA, SmashCharacter::class.java))
    }

}
