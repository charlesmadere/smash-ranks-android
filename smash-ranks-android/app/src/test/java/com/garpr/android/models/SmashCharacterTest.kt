package com.garpr.android.models

import com.garpr.android.BaseTest
import com.google.gson.Gson
import com.google.gson.JsonElement
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
        private const val JSON_BAYONETTA = "\"byo\""
        private const val JSON_BOWSER = "\"bow\""
        private const val JSON_BOWSER_JR = "\"bjr\""
        private const val JSON_CPTN_FALCON = "\"fcn\""
        private const val JSON_CPTN_OLIMAR = "\"olm\""
        private const val JSON_CHARIZARD = "\"chr\""
        private const val JSON_CLOUD = "\"cld\""
        private const val JSON_CORRIN = "\"crn\""
        private const val JSON_DARK_PIT = "\"dpt\""
        private const val JSON_DIDDY_KONG = "\"ddy\""
        private const val JSON_DONKEY_KONG = "\"dnk\""
        private const val JSON_DR_MARIO = "\"doc\""
        private const val JSON_DUCK_HUNT = "\"dck\""
        private const val JSON_FALCO = "\"fco\""
        private const val JSON_FOX = "\"fox\""
        private const val JSON_GANONDORF = "\"gnn\""
        private const val JSON_GRENINJA = "\"grn\""
        private const val JSON_ICE_CLIMBERS = "\"ics\""
        private const val JSON_IKE = "\"ike\""
        private const val JSON_INKLING = "\"ink\""
        private const val JSON_JIGGLYPUFF = "\"puf\""
        private const val JSON_KIRBY = "\"kby\""
        private const val JSON_KING_DEDEDE = "\"ddd\""
        private const val JSON_LINK = "\"lnk\""
        private const val JSON_LITTLE_MAC = "\"lmc\""
        private const val JSON_LUCARIO = "\"lcr\""
        private const val JSON_LUCAS = "\"lcs\""
        private const val JSON_LUCINA = "\"lcn\""
        private const val JSON_LUIGI = "\"lgi\""
        private const val JSON_MARIO = "\"mar\""
        private const val JSON_MARTH = "\"mrt\""
        private const val JSON_MEGA_MAN = "\"meg\""
        private const val JSON_META_KNIGHT = "\"mtk\""
        private const val JSON_MEWTWO = "\"mw2\""
        private const val JSON_MII_BRAWLER = "\"mib\""
        private const val JSON_MII_GUNNER = "\"mig\""
        private const val JSON_MII_SWORDFIGHTER = "\"mis\""
        private const val JSON_MR_GAME_AND_WATCH = "\"gnw\""
        private const val JSON_NESS = "\"nes\""
        private const val JSON_PAC_MAN = "\"pac\""
        private const val JSON_PALUTENA = "\"pal\""
        private const val JSON_PEACH = "\"pch\""
        private const val JSON_PICHU = "\"pic\""
        private const val JSON_PIKACHU = "\"pik\""
        private const val JSON_PIT = "\"pit\""
        private const val JSON_POKEMON_TRAINER = "\"pkt\""
        private const val JSON_RIDLEY = "\"rid\""
        private const val JSON_ROB = "\"rob\""
        private const val JSON_ROBIN = "\"rbn\""
        private const val JSON_ROSALINA = "\"ros\""
        private const val JSON_ROY = "\"roy\""
        private const val JSON_RYU = "\"ryu\""
        private const val JSON_SAMUS = "\"sam\""
        private const val JSON_SHEIK = "\"shk\""
        private const val JSON_SHULK = "\"slk\""
        private const val JSON_SNAKE = "\"snk\""
        private const val JSON_SQUIRTLE = "\"sqt\""
        private const val JSON_SONIC = "\"snc\""
        private const val JSON_TOON_LINK = "\"tlk\""
        private const val JSON_VILLAGER = "\"vlg\""
        private const val JSON_WARIO = "\"war\""
        private const val JSON_WII_FIT_TRAINER = "\"wft\""
        private const val JSON_WOLF = "\"wlf\""
        private const val JSON_YOSHI = "\"ysh\""
        private const val JSON_YOUNG_LINK = "\"ylk\""
        private const val JSON_ZELDA = "\"zld\""
        private const val JSON_ZERO_SUIT_SAMUS = "\"zss\""
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    fun testBayonetta() {
        assertEquals(JSON_BAYONETTA, gson.toJson(SmashCharacter.BAYONETTA))
        assertEquals(SmashCharacter.BAYONETTA, gson.fromJson(JSON_BAYONETTA, SmashCharacter::class.java))
    }

    @Test
    fun testBowser() {
        assertEquals(JSON_BOWSER, gson.toJson(SmashCharacter.BOWSER))
        assertEquals(SmashCharacter.BOWSER, gson.fromJson(JSON_BOWSER, SmashCharacter::class.java))
    }

    @Test
    fun testBowserJr() {
        assertEquals(JSON_BOWSER_JR, gson.toJson(SmashCharacter.BOWSER_JR))
        assertEquals(SmashCharacter.BOWSER_JR, gson.fromJson(JSON_BOWSER_JR, SmashCharacter::class.java))
    }

    @Test
    fun testCptnFalcon() {
        assertEquals(JSON_CPTN_FALCON, gson.toJson(SmashCharacter.CPTN_FALCON))
        assertEquals(SmashCharacter.CPTN_FALCON, gson.fromJson(JSON_CPTN_FALCON, SmashCharacter::class.java))
    }

    @Test
    fun testCptnOlimar() {
        assertEquals(JSON_CPTN_OLIMAR, gson.toJson(SmashCharacter.CPTN_OLIMAR))
        assertEquals(SmashCharacter.CPTN_OLIMAR, gson.fromJson(JSON_CPTN_OLIMAR, SmashCharacter::class.java))
    }

    @Test
    fun testCharizard() {
        assertEquals(JSON_CHARIZARD, gson.toJson(SmashCharacter.CHARIZARD))
        assertEquals(SmashCharacter.CHARIZARD, gson.fromJson(JSON_CHARIZARD, SmashCharacter::class.java))
    }

    @Test
    fun testCloud() {
        assertEquals(JSON_CLOUD, gson.toJson(SmashCharacter.CLOUD))
        assertEquals(SmashCharacter.CLOUD, gson.fromJson(JSON_CLOUD, SmashCharacter::class.java))
    }

    @Test
    fun testCorrin() {
        assertEquals(JSON_CORRIN, gson.toJson(SmashCharacter.CORRIN))
        assertEquals(SmashCharacter.CORRIN, gson.fromJson(JSON_CORRIN, SmashCharacter::class.java))
    }

    @Test
    fun testDarkPit() {
        assertEquals(JSON_DARK_PIT, gson.toJson(SmashCharacter.DARK_PIT))
        assertEquals(SmashCharacter.DARK_PIT, gson.fromJson(JSON_DARK_PIT, SmashCharacter::class.java))
    }

    @Test
    fun testDiddyKong() {
        assertEquals(JSON_DIDDY_KONG, gson.toJson(SmashCharacter.DIDDY_KONG))
        assertEquals(SmashCharacter.DIDDY_KONG, gson.fromJson(JSON_DIDDY_KONG, SmashCharacter::class.java))
    }

    @Test
    fun testDonkeyKong() {
        assertEquals(JSON_DONKEY_KONG, gson.toJson(SmashCharacter.DONKEY_KONG))
        assertEquals(SmashCharacter.DONKEY_KONG, gson.fromJson(JSON_DONKEY_KONG, SmashCharacter::class.java))
    }

    @Test
    fun testDrMario() {
        assertEquals(JSON_DR_MARIO, gson.toJson(SmashCharacter.DR_MARIO))
        assertEquals(SmashCharacter.DR_MARIO, gson.fromJson(JSON_DR_MARIO, SmashCharacter::class.java))
    }

    @Test
    fun testDuckHunt() {
        assertEquals(JSON_DUCK_HUNT, gson.toJson(SmashCharacter.DUCK_HUNT))
        assertEquals(SmashCharacter.DUCK_HUNT, gson.fromJson(JSON_DUCK_HUNT, SmashCharacter::class.java))
    }

    @Test
    fun testEmptyString() {
        assertNull(gson.fromJson("", SmashCharacter::class.java))
    }

    @Test
    fun testFalco() {
        assertEquals(JSON_FALCO, gson.toJson(SmashCharacter.FALCO))
        assertEquals(SmashCharacter.FALCO, gson.fromJson(JSON_FALCO, SmashCharacter::class.java))
    }

    @Test
    fun testFox() {
        assertEquals(JSON_FOX, gson.toJson(SmashCharacter.FOX))
        assertEquals(SmashCharacter.FOX, gson.fromJson(JSON_FOX, SmashCharacter::class.java))
    }

    @Test
    fun testGanondorf() {
        assertEquals(JSON_GANONDORF, gson.toJson(SmashCharacter.GANONDORF))
        assertEquals(SmashCharacter.GANONDORF, gson.fromJson(JSON_GANONDORF, SmashCharacter::class.java))
    }

    @Test
    fun testGreninja() {
        assertEquals(JSON_GRENINJA, gson.toJson(SmashCharacter.GRENINJA))
        assertEquals(SmashCharacter.GRENINJA, gson.fromJson(JSON_GRENINJA, SmashCharacter::class.java))
    }

    @Test
    fun testIceClimbers() {
        assertEquals(JSON_ICE_CLIMBERS, gson.toJson(SmashCharacter.ICE_CLIMBERS))
        assertEquals(SmashCharacter.ICE_CLIMBERS, gson.fromJson(JSON_ICE_CLIMBERS, SmashCharacter::class.java))
    }

    @Test
    fun testIke() {
        assertEquals(JSON_IKE, gson.toJson(SmashCharacter.IKE))
        assertEquals(SmashCharacter.IKE, gson.fromJson(JSON_IKE, SmashCharacter::class.java))
    }

    @Test
    fun testInkling() {
        assertEquals(JSON_INKLING, gson.toJson(SmashCharacter.INKLING))
        assertEquals(SmashCharacter.INKLING, gson.fromJson(JSON_INKLING, SmashCharacter::class.java))
    }

    @Test
    fun testJigglypuff() {
        assertEquals(JSON_JIGGLYPUFF, gson.toJson(SmashCharacter.JIGGLYPUFF))
        assertEquals(SmashCharacter.JIGGLYPUFF, gson.fromJson(JSON_JIGGLYPUFF, SmashCharacter::class.java))
    }

    @Test
    fun testKingDedede() {
        assertEquals(JSON_KING_DEDEDE, gson.toJson(SmashCharacter.KING_DEDEDE))
        assertEquals(SmashCharacter.KING_DEDEDE, gson.fromJson(JSON_KING_DEDEDE, SmashCharacter::class.java))
    }

    @Test
    fun testKirby() {
        assertEquals(JSON_KIRBY, gson.toJson(SmashCharacter.KIRBY))
        assertEquals(SmashCharacter.KIRBY, gson.fromJson(JSON_KIRBY, SmashCharacter::class.java))
    }

    @Test
    fun testLink() {
        assertEquals(JSON_LINK, gson.toJson(SmashCharacter.LINK))
        assertEquals(SmashCharacter.LINK, gson.fromJson(JSON_LINK, SmashCharacter::class.java))
    }

    @Test
    fun testLittleMac() {
        assertEquals(JSON_LITTLE_MAC, gson.toJson(SmashCharacter.LITTLE_MAC))
        assertEquals(SmashCharacter.LITTLE_MAC, gson.fromJson(JSON_LITTLE_MAC, SmashCharacter::class.java))
    }

    @Test
    fun testLucario() {
        assertEquals(JSON_LUCARIO, gson.toJson(SmashCharacter.LUCARIO))
        assertEquals(SmashCharacter.LUCARIO, gson.fromJson(JSON_LUCARIO, SmashCharacter::class.java))
    }

    @Test
    fun testLucas() {
        assertEquals(JSON_LUCAS, gson.toJson(SmashCharacter.LUCAS))
        assertEquals(SmashCharacter.LUCAS, gson.fromJson(JSON_LUCAS, SmashCharacter::class.java))
    }

    @Test
    fun testLucina() {
        assertEquals(JSON_LUCINA, gson.toJson(SmashCharacter.LUCINA))
        assertEquals(SmashCharacter.LUCINA, gson.fromJson(JSON_LUCINA, SmashCharacter::class.java))
    }

    @Test
    fun testLuigi() {
        assertEquals(JSON_LUIGI, gson.toJson(SmashCharacter.LUIGI))
        assertEquals(SmashCharacter.LUIGI, gson.fromJson(JSON_LUIGI, SmashCharacter::class.java))
    }

    @Test
    fun testMario() {
        assertEquals(JSON_MARIO, gson.toJson(SmashCharacter.MARIO))
        assertEquals(SmashCharacter.MARIO, gson.fromJson(JSON_MARIO, SmashCharacter::class.java))
    }

    @Test
    fun testMarth() {
        assertEquals(JSON_MARTH, gson.toJson(SmashCharacter.MARTH))
        assertEquals(SmashCharacter.MARTH, gson.fromJson(JSON_MARTH, SmashCharacter::class.java))
    }

    @Test
    fun testMegaMan() {
        assertEquals(JSON_MEGA_MAN, gson.toJson(SmashCharacter.MEGA_MAN))
        assertEquals(SmashCharacter.MEGA_MAN, gson.fromJson(JSON_MEGA_MAN, SmashCharacter::class.java))
    }

    @Test
    fun testMetaKnight() {
        assertEquals(JSON_META_KNIGHT, gson.toJson(SmashCharacter.META_KNIGHT))
        assertEquals(SmashCharacter.META_KNIGHT, gson.fromJson(JSON_META_KNIGHT, SmashCharacter::class.java))
    }

    @Test
    fun testMewtwo() {
        assertEquals(JSON_MEWTWO, gson.toJson(SmashCharacter.MEWTWO))
        assertEquals(SmashCharacter.MEWTWO, gson.fromJson(JSON_MEWTWO, SmashCharacter::class.java))
    }

    @Test
    fun testMiiBrawler() {
        assertEquals(JSON_MII_BRAWLER, gson.toJson(SmashCharacter.MII_BRAWLER))
        assertEquals(SmashCharacter.MII_BRAWLER, gson.fromJson(JSON_MII_BRAWLER, SmashCharacter::class.java))
    }

    @Test
    fun testMiiGunner() {
        assertEquals(JSON_MII_GUNNER, gson.toJson(SmashCharacter.MII_GUNNER))
        assertEquals(SmashCharacter.MII_GUNNER, gson.fromJson(JSON_MII_GUNNER, SmashCharacter::class.java))
    }

    @Test
    fun testMiiSwordFighter() {
        assertEquals(JSON_MII_SWORDFIGHTER, gson.toJson(SmashCharacter.MII_SWORDFIGHTER))
        assertEquals(SmashCharacter.MII_SWORDFIGHTER, gson.fromJson(JSON_MII_SWORDFIGHTER, SmashCharacter::class.java))
    }

    @Test
    fun testMrGameAndWatch() {
        assertEquals(JSON_MR_GAME_AND_WATCH, gson.toJson(SmashCharacter.MR_GAME_AND_WATCH))
        assertEquals(SmashCharacter.MR_GAME_AND_WATCH, gson.fromJson(JSON_MR_GAME_AND_WATCH, SmashCharacter::class.java))
    }

    @Test
    fun testNess() {
        assertEquals(JSON_NESS, gson.toJson(SmashCharacter.NESS))
        assertEquals(SmashCharacter.NESS, gson.fromJson(JSON_NESS, SmashCharacter::class.java))
    }

    @Test
    fun testNullJsonElement() {
        assertNull(gson.fromJson(null as JsonElement?, SmashCharacter::class.java))
    }

    @Test
    fun testNullString() {
        assertNull(gson.fromJson(null as String?, SmashCharacter::class.java))
    }

    @Test
    fun testPacMan() {
        assertEquals(JSON_PAC_MAN, gson.toJson(SmashCharacter.PAC_MAN))
        assertEquals(SmashCharacter.PAC_MAN, gson.fromJson(JSON_PAC_MAN, SmashCharacter::class.java))
    }

    @Test
    fun testPalutena() {
        assertEquals(JSON_PALUTENA, gson.toJson(SmashCharacter.PALUTENA))
        assertEquals(SmashCharacter.PALUTENA, gson.fromJson(JSON_PALUTENA, SmashCharacter::class.java))
    }

    @Test
    fun testPeach() {
        assertEquals(JSON_PEACH, gson.toJson(SmashCharacter.PEACH))
        assertEquals(SmashCharacter.PEACH, gson.fromJson(JSON_PEACH, SmashCharacter::class.java))
    }

    @Test
    fun testPichu() {
        assertEquals(JSON_PICHU, gson.toJson(SmashCharacter.PICHU))
        assertEquals(SmashCharacter.PICHU, gson.fromJson(JSON_PICHU, SmashCharacter::class.java))
    }

    @Test
    fun testPikachu() {
        assertEquals(JSON_PIKACHU, gson.toJson(SmashCharacter.PIKACHU))
        assertEquals(SmashCharacter.PIKACHU, gson.fromJson(JSON_PIKACHU, SmashCharacter::class.java))
    }

    @Test
    fun testPit() {
        assertEquals(JSON_PIT, gson.toJson(SmashCharacter.PIT))
        assertEquals(SmashCharacter.PIT, gson.fromJson(JSON_PIT, SmashCharacter::class.java))
    }

    @Test
    fun testPokemonTrainer() {
        assertEquals(JSON_POKEMON_TRAINER, gson.toJson(SmashCharacter.POKEMON_TRAINER))
        assertEquals(SmashCharacter.POKEMON_TRAINER, gson.fromJson(JSON_POKEMON_TRAINER, SmashCharacter::class.java))
    }

    @Test
    fun testRidley() {
        assertEquals(JSON_RIDLEY, gson.toJson(SmashCharacter.RIDLEY))
        assertEquals(SmashCharacter.RIDLEY, gson.fromJson(JSON_RIDLEY, SmashCharacter::class.java))
    }

    @Test
    fun testRob() {
        assertEquals(JSON_ROB, gson.toJson(SmashCharacter.ROB))
        assertEquals(SmashCharacter.ROB, gson.fromJson(JSON_ROB, SmashCharacter::class.java))
    }

    @Test
    fun testRobin() {
        assertEquals(JSON_ROBIN, gson.toJson(SmashCharacter.ROBIN))
        assertEquals(SmashCharacter.ROBIN, gson.fromJson(JSON_ROBIN, SmashCharacter::class.java))
    }

    @Test
    fun testRosalina() {
        assertEquals(JSON_ROSALINA, gson.toJson(SmashCharacter.ROSALINA))
        assertEquals(SmashCharacter.ROSALINA, gson.fromJson(JSON_ROSALINA, SmashCharacter::class.java))
    }

    @Test
    fun testRoy() {
        assertEquals(JSON_ROY, gson.toJson(SmashCharacter.ROY))
        assertEquals(SmashCharacter.ROY, gson.fromJson(JSON_ROY, SmashCharacter::class.java))
    }

    @Test
    fun testRyu() {
        assertEquals(JSON_RYU, gson.toJson(SmashCharacter.RYU))
        assertEquals(SmashCharacter.RYU, gson.fromJson(JSON_RYU, SmashCharacter::class.java))
    }

    @Test
    fun testSamus() {
        assertEquals(JSON_SAMUS, gson.toJson(SmashCharacter.SAMUS))
        assertEquals(SmashCharacter.SAMUS, gson.fromJson(JSON_SAMUS, SmashCharacter::class.java))
    }

    @Test
    fun testSheik() {
        assertEquals(JSON_SHEIK, gson.toJson(SmashCharacter.SHEIK))
        assertEquals(SmashCharacter.SHEIK, gson.fromJson(JSON_SHEIK, SmashCharacter::class.java))
    }

    @Test
    fun testShulk() {
        assertEquals(JSON_SHULK, gson.toJson(SmashCharacter.SHULK))
        assertEquals(SmashCharacter.SHULK, gson.fromJson(JSON_SHULK, SmashCharacter::class.java))
    }

    @Test
    fun testSnake() {
        assertEquals(JSON_SNAKE, gson.toJson(SmashCharacter.SNAKE))
        assertEquals(SmashCharacter.SNAKE, gson.fromJson(JSON_SNAKE, SmashCharacter::class.java))
    }

    @Test
    fun testSonic() {
        assertEquals(JSON_SONIC, gson.toJson(SmashCharacter.SONIC))
        assertEquals(SmashCharacter.SONIC, gson.fromJson(JSON_SONIC, SmashCharacter::class.java))
    }

    @Test
    fun testSquirtle() {
        assertEquals(JSON_SQUIRTLE, gson.toJson(SmashCharacter.SQUIRTLE))
        assertEquals(SmashCharacter.SQUIRTLE, gson.fromJson(JSON_SQUIRTLE, SmashCharacter::class.java))
    }

    @Test
    fun testToonLink() {
        assertEquals(JSON_TOON_LINK, gson.toJson(SmashCharacter.TOON_LINK))
        assertEquals(SmashCharacter.TOON_LINK, gson.fromJson(JSON_TOON_LINK, SmashCharacter::class.java))
    }

    @Test
    fun testVillager() {
        assertEquals(JSON_VILLAGER, gson.toJson(SmashCharacter.VILLAGER))
        assertEquals(SmashCharacter.VILLAGER, gson.fromJson(JSON_VILLAGER, SmashCharacter::class.java))
    }

    @Test
    fun testWario() {
        assertEquals(JSON_WARIO, gson.toJson(SmashCharacter.WARIO))
        assertEquals(SmashCharacter.WARIO, gson.fromJson(JSON_WARIO, SmashCharacter::class.java))
    }

    @Test
    fun testWiiFitTrainer() {
        assertEquals(JSON_WII_FIT_TRAINER, gson.toJson(SmashCharacter.WII_FIT_TRAINER))
        assertEquals(SmashCharacter.WII_FIT_TRAINER, gson.fromJson(JSON_WII_FIT_TRAINER, SmashCharacter::class.java))
    }

    @Test
    fun testWolf() {
        assertEquals(JSON_WOLF, gson.toJson(SmashCharacter.WOLF))
        assertEquals(SmashCharacter.WOLF, gson.fromJson(JSON_WOLF, SmashCharacter::class.java))
    }

    @Test
    fun testYoshi() {
        assertEquals(JSON_YOSHI, gson.toJson(SmashCharacter.YOSHI))
        assertEquals(SmashCharacter.YOSHI, gson.fromJson(JSON_YOSHI, SmashCharacter::class.java))
    }

    @Test
    fun testYoungLink() {
        assertEquals(JSON_YOUNG_LINK, gson.toJson(SmashCharacter.YOUNG_LINK))
        assertEquals(SmashCharacter.YOUNG_LINK, gson.fromJson(JSON_YOUNG_LINK, SmashCharacter::class.java))
    }

    @Test
    fun testZelda() {
        assertEquals(JSON_ZELDA, gson.toJson(SmashCharacter.ZELDA))
        assertEquals(SmashCharacter.ZELDA, gson.fromJson(JSON_ZELDA, SmashCharacter::class.java))
    }

    @Test
    fun testZeroSuitSamus() {
        assertEquals(JSON_ZERO_SUIT_SAMUS, gson.toJson(SmashCharacter.ZERO_SUIT_SAMUS))
        assertEquals(SmashCharacter.ZERO_SUIT_SAMUS, gson.fromJson(JSON_ZERO_SUIT_SAMUS, SmashCharacter::class.java))
    }

}
