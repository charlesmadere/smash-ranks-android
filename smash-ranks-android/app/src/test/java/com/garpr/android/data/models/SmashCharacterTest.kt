package com.garpr.android.data.models

import com.garpr.android.BaseTest
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SmashCharacterTest : BaseTest() {

    protected val moshi: Moshi by inject()

    private lateinit var smashCharacterAdapter: JsonAdapter<SmashCharacter>

    companion object {
        private const val JSON_BANJO_N_KAZOOIE = "\"bnk\""
        private const val JSON_BAYONETTA = "\"byo\""
        private const val JSON_BOWSER = "\"bow\""
        private const val JSON_BOWSER_JR = "\"bjr\""
        private const val JSON_CPTN_FALCON = "\"fcn\""
        private const val JSON_CPTN_OLIMAR = "\"olm\""
        private const val JSON_CHARIZARD = "\"chr\""
        private const val JSON_CHROM = "\"chm\""
        private const val JSON_CLOUD = "\"cld\""
        private const val JSON_CORRIN = "\"crn\""
        private const val JSON_DARK_PIT = "\"dpt\""
        private const val JSON_DARK_SAMUS = "\"dks\""
        private const val JSON_DIDDY_KONG = "\"ddy\""
        private const val JSON_DONKEY_KONG = "\"dnk\""
        private const val JSON_DR_MARIO = "\"doc\""
        private const val JSON_DUCK_HUNT = "\"dck\""
        private const val JSON_FALCO = "\"fco\""
        private const val JSON_FOX = "\"fox\""
        private const val JSON_GANONDORF = "\"gnn\""
        private const val JSON_GRENINJA = "\"grn\""
        private const val JSON_HERO = "\"hro\""
        private const val JSON_ICE_CLIMBERS = "\"ics\""
        private const val JSON_IKE = "\"ike\""
        private const val JSON_INCINEROAR = "\"inc\""
        private const val JSON_INKLING = "\"ink\""
        private const val JSON_IVYSAUR = "\"ivy\""
        private const val JSON_JIGGLYPUFF = "\"puf\""
        private const val JSON_JOKER = "\"jok\""
        private const val JSON_KEN = "\"ken\""
        private const val JSON_KIRBY = "\"kby\""
        private const val JSON_KING_DEDEDE = "\"ddd\""
        private const val JSON_KING_K_ROOL = "\"kkr\""
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
        private const val JSON_PIRANHA_PLANT = "\"pir\""
        private const val JSON_PIT = "\"pit\""
        private const val JSON_POKEMON_TRAINER = "\"pkt\""
        private const val JSON_RICTER = "\"ric\""
        private const val JSON_RIDLEY = "\"rid\""
        private const val JSON_ROB = "\"rob\""
        private const val JSON_ROBIN = "\"rbn\""
        private const val JSON_ROSALINA = "\"ros\""
        private const val JSON_ROY = "\"roy\""
        private const val JSON_RYU = "\"ryu\""
        private const val JSON_SAMUS = "\"sam\""
        private const val JSON_SHEIK = "\"shk\""
        private const val JSON_SHULK = "\"slk\""
        private const val JSON_SIMON = "\"smn\""
        private const val JSON_SNAKE = "\"snk\""
        private const val JSON_SQUIRTLE = "\"sqt\""
        private const val JSON_SONIC = "\"snc\""
        private const val JSON_TERRY_BOGARD = "\"tbg\""
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

        smashCharacterAdapter = moshi.adapter(SmashCharacter::class.java)
    }

    @Test
    fun testBanjoAndKazooie() {
        assertEquals(JSON_BANJO_N_KAZOOIE, smashCharacterAdapter.toJson(SmashCharacter.BANJO_N_KAZOOIE))
        assertEquals(SmashCharacter.BANJO_N_KAZOOIE, smashCharacterAdapter.fromJson(JSON_BANJO_N_KAZOOIE))
    }

    @Test
    fun testBayonetta() {
        assertEquals(JSON_BAYONETTA, smashCharacterAdapter.toJson(SmashCharacter.BAYONETTA))
        assertEquals(SmashCharacter.BAYONETTA, smashCharacterAdapter.fromJson(JSON_BAYONETTA))
    }

    @Test
    fun testBowser() {
        assertEquals(JSON_BOWSER, smashCharacterAdapter.toJson(SmashCharacter.BOWSER))
        assertEquals(SmashCharacter.BOWSER, smashCharacterAdapter.fromJson(JSON_BOWSER))
    }

    @Test
    fun testBowserJr() {
        assertEquals(JSON_BOWSER_JR, smashCharacterAdapter.toJson(SmashCharacter.BOWSER_JR))
        assertEquals(SmashCharacter.BOWSER_JR, smashCharacterAdapter.fromJson(JSON_BOWSER_JR))
    }

    @Test
    fun testCptnFalcon() {
        assertEquals(JSON_CPTN_FALCON, smashCharacterAdapter.toJson(SmashCharacter.CPTN_FALCON))
        assertEquals(SmashCharacter.CPTN_FALCON, smashCharacterAdapter.fromJson(JSON_CPTN_FALCON))
    }

    @Test
    fun testCptnOlimar() {
        assertEquals(JSON_CPTN_OLIMAR, smashCharacterAdapter.toJson(SmashCharacter.CPTN_OLIMAR))
        assertEquals(SmashCharacter.CPTN_OLIMAR, smashCharacterAdapter.fromJson(JSON_CPTN_OLIMAR))
    }

    @Test
    fun testCharizard() {
        assertEquals(JSON_CHARIZARD, smashCharacterAdapter.toJson(SmashCharacter.CHARIZARD))
        assertEquals(SmashCharacter.CHARIZARD, smashCharacterAdapter.fromJson(JSON_CHARIZARD))
    }

    @Test
    fun testChrom() {
        assertEquals(JSON_CHROM, smashCharacterAdapter.toJson(SmashCharacter.CHROM))
        assertEquals(SmashCharacter.CHROM, smashCharacterAdapter.fromJson(JSON_CHROM))
    }

    @Test
    fun testCloud() {
        assertEquals(JSON_CLOUD, smashCharacterAdapter.toJson(SmashCharacter.CLOUD))
        assertEquals(SmashCharacter.CLOUD, smashCharacterAdapter.fromJson(JSON_CLOUD))
    }

    @Test
    fun testCorrin() {
        assertEquals(JSON_CORRIN, smashCharacterAdapter.toJson(SmashCharacter.CORRIN))
        assertEquals(SmashCharacter.CORRIN, smashCharacterAdapter.fromJson(JSON_CORRIN))
    }

    @Test
    fun testDarkPit() {
        assertEquals(JSON_DARK_PIT, smashCharacterAdapter.toJson(SmashCharacter.DARK_PIT))
        assertEquals(SmashCharacter.DARK_PIT, smashCharacterAdapter.fromJson(JSON_DARK_PIT))
    }

    @Test
    fun testDarkSamus() {
        assertEquals(JSON_DARK_SAMUS, smashCharacterAdapter.toJson(SmashCharacter.DARK_SAMUS))
        assertEquals(SmashCharacter.DARK_SAMUS, smashCharacterAdapter.fromJson(JSON_DARK_SAMUS))
    }

    @Test
    fun testDiddyKong() {
        assertEquals(JSON_DIDDY_KONG, smashCharacterAdapter.toJson(SmashCharacter.DIDDY_KONG))
        assertEquals(SmashCharacter.DIDDY_KONG, smashCharacterAdapter.fromJson(JSON_DIDDY_KONG))
    }

    @Test
    fun testDonkeyKong() {
        assertEquals(JSON_DONKEY_KONG, smashCharacterAdapter.toJson(SmashCharacter.DONKEY_KONG))
        assertEquals(SmashCharacter.DONKEY_KONG, smashCharacterAdapter.fromJson(JSON_DONKEY_KONG))
    }

    @Test
    fun testDrMario() {
        assertEquals(JSON_DR_MARIO, smashCharacterAdapter.toJson(SmashCharacter.DR_MARIO))
        assertEquals(SmashCharacter.DR_MARIO, smashCharacterAdapter.fromJson(JSON_DR_MARIO))
    }

    @Test
    fun testDuckHunt() {
        assertEquals(JSON_DUCK_HUNT, smashCharacterAdapter.toJson(SmashCharacter.DUCK_HUNT))
        assertEquals(SmashCharacter.DUCK_HUNT, smashCharacterAdapter.fromJson(JSON_DUCK_HUNT))
    }

    @Test
    fun testFalco() {
        assertEquals(JSON_FALCO, smashCharacterAdapter.toJson(SmashCharacter.FALCO))
        assertEquals(SmashCharacter.FALCO, smashCharacterAdapter.fromJson(JSON_FALCO))
    }

    @Test
    fun testFox() {
        assertEquals(JSON_FOX, smashCharacterAdapter.toJson(SmashCharacter.FOX))
        assertEquals(SmashCharacter.FOX, smashCharacterAdapter.fromJson(JSON_FOX))
    }

    @Test
    fun testGanondorf() {
        assertEquals(JSON_GANONDORF, smashCharacterAdapter.toJson(SmashCharacter.GANONDORF))
        assertEquals(SmashCharacter.GANONDORF, smashCharacterAdapter.fromJson(JSON_GANONDORF))
    }

    @Test
    fun testGreninja() {
        assertEquals(JSON_GRENINJA, smashCharacterAdapter.toJson(SmashCharacter.GRENINJA))
        assertEquals(SmashCharacter.GRENINJA, smashCharacterAdapter.fromJson(JSON_GRENINJA))
    }

    @Test
    fun testHero() {
        assertEquals(JSON_HERO, smashCharacterAdapter.toJson(SmashCharacter.HERO))
        assertEquals(SmashCharacter.HERO, smashCharacterAdapter.fromJson(JSON_HERO))
    }

    @Test
    fun testIceClimbers() {
        assertEquals(JSON_ICE_CLIMBERS, smashCharacterAdapter.toJson(SmashCharacter.ICE_CLIMBERS))
        assertEquals(SmashCharacter.ICE_CLIMBERS, smashCharacterAdapter.fromJson(JSON_ICE_CLIMBERS))
    }

    @Test
    fun testIke() {
        assertEquals(JSON_IKE, smashCharacterAdapter.toJson(SmashCharacter.IKE))
        assertEquals(SmashCharacter.IKE, smashCharacterAdapter.fromJson(JSON_IKE))
    }

    @Test
    fun testIncineroar() {
        assertEquals(JSON_INCINEROAR, smashCharacterAdapter.toJson(SmashCharacter.INCINEROAR))
        assertEquals(SmashCharacter.INCINEROAR, smashCharacterAdapter.fromJson(JSON_INCINEROAR))
    }

    @Test
    fun testInkling() {
        assertEquals(JSON_INKLING, smashCharacterAdapter.toJson(SmashCharacter.INKLING))
        assertEquals(SmashCharacter.INKLING, smashCharacterAdapter.fromJson(JSON_INKLING))
    }

    @Test
    fun testIvysaur() {
        assertEquals(JSON_IVYSAUR, smashCharacterAdapter.toJson(SmashCharacter.IVYSAUR))
        assertEquals(SmashCharacter.IVYSAUR, smashCharacterAdapter.fromJson(JSON_IVYSAUR))
    }

    @Test
    fun testJigglypuff() {
        assertEquals(JSON_JIGGLYPUFF, smashCharacterAdapter.toJson(SmashCharacter.JIGGLYPUFF))
        assertEquals(SmashCharacter.JIGGLYPUFF, smashCharacterAdapter.fromJson(JSON_JIGGLYPUFF))
    }

    @Test
    fun testJoker() {
        assertEquals(JSON_JOKER, smashCharacterAdapter.toJson(SmashCharacter.JOKER))
        assertEquals(SmashCharacter.JOKER, smashCharacterAdapter.fromJson(JSON_JOKER))
    }

    @Test
    fun testKen() {
        assertEquals(JSON_KEN, smashCharacterAdapter.toJson(SmashCharacter.KEN))
        assertEquals(SmashCharacter.KEN, smashCharacterAdapter.fromJson(JSON_KEN))
    }

    @Test
    fun testKingDedede() {
        assertEquals(JSON_KING_DEDEDE, smashCharacterAdapter.toJson(SmashCharacter.KING_DEDEDE))
        assertEquals(SmashCharacter.KING_DEDEDE, smashCharacterAdapter.fromJson(JSON_KING_DEDEDE))
    }

    @Test
    fun testKingKRool() {
        assertEquals(JSON_KING_K_ROOL, smashCharacterAdapter.toJson(SmashCharacter.KING_K_ROOL))
        assertEquals(SmashCharacter.KING_K_ROOL, smashCharacterAdapter.fromJson(JSON_KING_K_ROOL))
    }

    @Test
    fun testKirby() {
        assertEquals(JSON_KIRBY, smashCharacterAdapter.toJson(SmashCharacter.KIRBY))
        assertEquals(SmashCharacter.KIRBY, smashCharacterAdapter.fromJson(JSON_KIRBY))
    }

    @Test
    fun testLink() {
        assertEquals(JSON_LINK, smashCharacterAdapter.toJson(SmashCharacter.LINK))
        assertEquals(SmashCharacter.LINK, smashCharacterAdapter.fromJson(JSON_LINK))
    }

    @Test
    fun testLittleMac() {
        assertEquals(JSON_LITTLE_MAC, smashCharacterAdapter.toJson(SmashCharacter.LITTLE_MAC))
        assertEquals(SmashCharacter.LITTLE_MAC, smashCharacterAdapter.fromJson(JSON_LITTLE_MAC))
    }

    @Test
    fun testLucario() {
        assertEquals(JSON_LUCARIO, smashCharacterAdapter.toJson(SmashCharacter.LUCARIO))
        assertEquals(SmashCharacter.LUCARIO, smashCharacterAdapter.fromJson(JSON_LUCARIO))
    }

    @Test
    fun testLucas() {
        assertEquals(JSON_LUCAS, smashCharacterAdapter.toJson(SmashCharacter.LUCAS))
        assertEquals(SmashCharacter.LUCAS, smashCharacterAdapter.fromJson(JSON_LUCAS))
    }

    @Test
    fun testLucina() {
        assertEquals(JSON_LUCINA, smashCharacterAdapter.toJson(SmashCharacter.LUCINA))
        assertEquals(SmashCharacter.LUCINA, smashCharacterAdapter.fromJson(JSON_LUCINA))
    }

    @Test
    fun testLuigi() {
        assertEquals(JSON_LUIGI, smashCharacterAdapter.toJson(SmashCharacter.LUIGI))
        assertEquals(SmashCharacter.LUIGI, smashCharacterAdapter.fromJson(JSON_LUIGI))
    }

    @Test
    fun testMario() {
        assertEquals(JSON_MARIO, smashCharacterAdapter.toJson(SmashCharacter.MARIO))
        assertEquals(SmashCharacter.MARIO, smashCharacterAdapter.fromJson(JSON_MARIO))
    }

    @Test
    fun testMarth() {
        assertEquals(JSON_MARTH, smashCharacterAdapter.toJson(SmashCharacter.MARTH))
        assertEquals(SmashCharacter.MARTH, smashCharacterAdapter.fromJson(JSON_MARTH))
    }

    @Test
    fun testMegaMan() {
        assertEquals(JSON_MEGA_MAN, smashCharacterAdapter.toJson(SmashCharacter.MEGA_MAN))
        assertEquals(SmashCharacter.MEGA_MAN, smashCharacterAdapter.fromJson(JSON_MEGA_MAN))
    }

    @Test
    fun testMetaKnight() {
        assertEquals(JSON_META_KNIGHT, smashCharacterAdapter.toJson(SmashCharacter.META_KNIGHT))
        assertEquals(SmashCharacter.META_KNIGHT, smashCharacterAdapter.fromJson(JSON_META_KNIGHT))
    }

    @Test
    fun testMewtwo() {
        assertEquals(JSON_MEWTWO, smashCharacterAdapter.toJson(SmashCharacter.MEWTWO))
        assertEquals(SmashCharacter.MEWTWO, smashCharacterAdapter.fromJson(JSON_MEWTWO))
    }

    @Test
    fun testMiiBrawler() {
        assertEquals(JSON_MII_BRAWLER, smashCharacterAdapter.toJson(SmashCharacter.MII_BRAWLER))
        assertEquals(SmashCharacter.MII_BRAWLER, smashCharacterAdapter.fromJson(JSON_MII_BRAWLER))
    }

    @Test
    fun testMiiGunner() {
        assertEquals(JSON_MII_GUNNER, smashCharacterAdapter.toJson(SmashCharacter.MII_GUNNER))
        assertEquals(SmashCharacter.MII_GUNNER, smashCharacterAdapter.fromJson(JSON_MII_GUNNER))
    }

    @Test
    fun testMiiSwordFighter() {
        assertEquals(JSON_MII_SWORDFIGHTER, smashCharacterAdapter.toJson(SmashCharacter.MII_SWORDFIGHTER))
        assertEquals(SmashCharacter.MII_SWORDFIGHTER, smashCharacterAdapter.fromJson(JSON_MII_SWORDFIGHTER))
    }

    @Test
    fun testMrGameAndWatch() {
        assertEquals(JSON_MR_GAME_AND_WATCH, smashCharacterAdapter.toJson(SmashCharacter.MR_GAME_AND_WATCH))
        assertEquals(SmashCharacter.MR_GAME_AND_WATCH, smashCharacterAdapter.fromJson(JSON_MR_GAME_AND_WATCH))
    }

    @Test
    fun testNess() {
        assertEquals(JSON_NESS, smashCharacterAdapter.toJson(SmashCharacter.NESS))
        assertEquals(SmashCharacter.NESS, smashCharacterAdapter.fromJson(JSON_NESS))
    }

    @Test
    fun testPacMan() {
        assertEquals(JSON_PAC_MAN, smashCharacterAdapter.toJson(SmashCharacter.PAC_MAN))
        assertEquals(SmashCharacter.PAC_MAN, smashCharacterAdapter.fromJson(JSON_PAC_MAN))
    }

    @Test
    fun testPalutena() {
        assertEquals(JSON_PALUTENA, smashCharacterAdapter.toJson(SmashCharacter.PALUTENA))
        assertEquals(SmashCharacter.PALUTENA, smashCharacterAdapter.fromJson(JSON_PALUTENA))
    }

    @Test
    fun testPeach() {
        assertEquals(JSON_PEACH, smashCharacterAdapter.toJson(SmashCharacter.PEACH))
        assertEquals(SmashCharacter.PEACH, smashCharacterAdapter.fromJson(JSON_PEACH))
    }

    @Test
    fun testPichu() {
        assertEquals(JSON_PICHU, smashCharacterAdapter.toJson(SmashCharacter.PICHU))
        assertEquals(SmashCharacter.PICHU, smashCharacterAdapter.fromJson(JSON_PICHU))
    }

    @Test
    fun testPikachu() {
        assertEquals(JSON_PIKACHU, smashCharacterAdapter.toJson(SmashCharacter.PIKACHU))
        assertEquals(SmashCharacter.PIKACHU, smashCharacterAdapter.fromJson(JSON_PIKACHU))
    }

    @Test
    fun testPiranhaPlant() {
        assertEquals(JSON_PIRANHA_PLANT, smashCharacterAdapter.toJson(SmashCharacter.PIRANHA_PLANT))
        assertEquals(SmashCharacter.PIRANHA_PLANT, smashCharacterAdapter.fromJson(JSON_PIRANHA_PLANT))
    }

    @Test
    fun testPit() {
        assertEquals(JSON_PIT, smashCharacterAdapter.toJson(SmashCharacter.PIT))
        assertEquals(SmashCharacter.PIT, smashCharacterAdapter.fromJson(JSON_PIT))
    }

    @Test
    fun testPokemonTrainer() {
        assertEquals(JSON_POKEMON_TRAINER, smashCharacterAdapter.toJson(SmashCharacter.POKEMON_TRAINER))
        assertEquals(SmashCharacter.POKEMON_TRAINER, smashCharacterAdapter.fromJson(JSON_POKEMON_TRAINER))
    }

    @Test
    fun testRicter() {
        assertEquals(JSON_RICTER, smashCharacterAdapter.toJson(SmashCharacter.RICTER))
        assertEquals(SmashCharacter.RICTER, smashCharacterAdapter.fromJson(JSON_RICTER))
    }

    @Test
    fun testRidley() {
        assertEquals(JSON_RIDLEY, smashCharacterAdapter.toJson(SmashCharacter.RIDLEY))
        assertEquals(SmashCharacter.RIDLEY, smashCharacterAdapter.fromJson(JSON_RIDLEY))
    }

    @Test
    fun testRob() {
        assertEquals(JSON_ROB, smashCharacterAdapter.toJson(SmashCharacter.ROB))
        assertEquals(SmashCharacter.ROB, smashCharacterAdapter.fromJson(JSON_ROB))
    }

    @Test
    fun testRobin() {
        assertEquals(JSON_ROBIN, smashCharacterAdapter.toJson(SmashCharacter.ROBIN))
        assertEquals(SmashCharacter.ROBIN, smashCharacterAdapter.fromJson(JSON_ROBIN))
    }

    @Test
    fun testRosalina() {
        assertEquals(JSON_ROSALINA, smashCharacterAdapter.toJson(SmashCharacter.ROSALINA))
        assertEquals(SmashCharacter.ROSALINA, smashCharacterAdapter.fromJson(JSON_ROSALINA))
    }

    @Test
    fun testRoy() {
        assertEquals(JSON_ROY, smashCharacterAdapter.toJson(SmashCharacter.ROY))
        assertEquals(SmashCharacter.ROY, smashCharacterAdapter.fromJson(JSON_ROY))
    }

    @Test
    fun testRyu() {
        assertEquals(JSON_RYU, smashCharacterAdapter.toJson(SmashCharacter.RYU))
        assertEquals(SmashCharacter.RYU, smashCharacterAdapter.fromJson(JSON_RYU))
    }

    @Test
    fun testSamus() {
        assertEquals(JSON_SAMUS, smashCharacterAdapter.toJson(SmashCharacter.SAMUS))
        assertEquals(SmashCharacter.SAMUS, smashCharacterAdapter.fromJson(JSON_SAMUS))
    }

    @Test
    fun testSheik() {
        assertEquals(JSON_SHEIK, smashCharacterAdapter.toJson(SmashCharacter.SHEIK))
        assertEquals(SmashCharacter.SHEIK, smashCharacterAdapter.fromJson(JSON_SHEIK))
    }

    @Test
    fun testShulk() {
        assertEquals(JSON_SHULK, smashCharacterAdapter.toJson(SmashCharacter.SHULK))
        assertEquals(SmashCharacter.SHULK, smashCharacterAdapter.fromJson(JSON_SHULK))
    }

    @Test
    fun testSimon() {
        assertEquals(JSON_SIMON, smashCharacterAdapter.toJson(SmashCharacter.SIMON))
        assertEquals(SmashCharacter.SIMON, smashCharacterAdapter.fromJson(JSON_SIMON))
    }

    @Test
    fun testSnake() {
        assertEquals(JSON_SNAKE, smashCharacterAdapter.toJson(SmashCharacter.SNAKE))
        assertEquals(SmashCharacter.SNAKE, smashCharacterAdapter.fromJson(JSON_SNAKE))
    }

    @Test
    fun testSonic() {
        assertEquals(JSON_SONIC, smashCharacterAdapter.toJson(SmashCharacter.SONIC))
        assertEquals(SmashCharacter.SONIC, smashCharacterAdapter.fromJson(JSON_SONIC))
    }

    @Test
    fun testSquirtle() {
        assertEquals(JSON_SQUIRTLE, smashCharacterAdapter.toJson(SmashCharacter.SQUIRTLE))
        assertEquals(SmashCharacter.SQUIRTLE, smashCharacterAdapter.fromJson(JSON_SQUIRTLE))
    }

    @Test
    fun testTerryBogard() {
        assertEquals(JSON_TERRY_BOGARD, smashCharacterAdapter.toJson(SmashCharacter.TERRY_BOGARD))
        assertEquals(SmashCharacter.TERRY_BOGARD, smashCharacterAdapter.fromJson(JSON_TERRY_BOGARD))
    }

    @Test
    fun testToonLink() {
        assertEquals(JSON_TOON_LINK, smashCharacterAdapter.toJson(SmashCharacter.TOON_LINK))
        assertEquals(SmashCharacter.TOON_LINK, smashCharacterAdapter.fromJson(JSON_TOON_LINK))
    }

    @Test
    fun testVillager() {
        assertEquals(JSON_VILLAGER, smashCharacterAdapter.toJson(SmashCharacter.VILLAGER))
        assertEquals(SmashCharacter.VILLAGER, smashCharacterAdapter.fromJson(JSON_VILLAGER))
    }

    @Test
    fun testWario() {
        assertEquals(JSON_WARIO, smashCharacterAdapter.toJson(SmashCharacter.WARIO))
        assertEquals(SmashCharacter.WARIO, smashCharacterAdapter.fromJson(JSON_WARIO))
    }

    @Test
    fun testWiiFitTrainer() {
        assertEquals(JSON_WII_FIT_TRAINER, smashCharacterAdapter.toJson(SmashCharacter.WII_FIT_TRAINER))
        assertEquals(SmashCharacter.WII_FIT_TRAINER, smashCharacterAdapter.fromJson(JSON_WII_FIT_TRAINER))
    }

    @Test
    fun testWolf() {
        assertEquals(JSON_WOLF, smashCharacterAdapter.toJson(SmashCharacter.WOLF))
        assertEquals(SmashCharacter.WOLF, smashCharacterAdapter.fromJson(JSON_WOLF))
    }

    @Test
    fun testYoshi() {
        assertEquals(JSON_YOSHI, smashCharacterAdapter.toJson(SmashCharacter.YOSHI))
        assertEquals(SmashCharacter.YOSHI, smashCharacterAdapter.fromJson(JSON_YOSHI))
    }

    @Test
    fun testYoungLink() {
        assertEquals(JSON_YOUNG_LINK, smashCharacterAdapter.toJson(SmashCharacter.YOUNG_LINK))
        assertEquals(SmashCharacter.YOUNG_LINK, smashCharacterAdapter.fromJson(JSON_YOUNG_LINK))
    }

    @Test
    fun testZelda() {
        assertEquals(JSON_ZELDA, smashCharacterAdapter.toJson(SmashCharacter.ZELDA))
        assertEquals(SmashCharacter.ZELDA, smashCharacterAdapter.fromJson(JSON_ZELDA))
    }

    @Test
    fun testZeroSuitSamus() {
        assertEquals(JSON_ZERO_SUIT_SAMUS, smashCharacterAdapter.toJson(SmashCharacter.ZERO_SUIT_SAMUS))
        assertEquals(SmashCharacter.ZERO_SUIT_SAMUS, smashCharacterAdapter.fromJson(JSON_ZERO_SUIT_SAMUS))
    }

}
