package com.garpr.android.data.models

import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BracketSourceTest : BaseTest() {

    @Test
    fun testFromUrlWithBing() {
        assertNull(BracketSource.fromUrl("https://www.bing.com/"))
    }

    @Test
    fun testFromUrlWithChallonge1() {
        assertEquals(BracketSource.CHALLONGE,
                BracketSource.fromUrl("http://challonge.com/stanfordmelee7"))
    }

    @Test
    fun testFromUrlWithChallonge2() {
        assertEquals(BracketSource.CHALLONGE,
                BracketSource.fromUrl("http://afkgg.challonge.com/afk17"))
    }

    @Test
    fun testFromUrlWithChallonge3() {
        assertEquals(BracketSource.CHALLONGE,
                BracketSource.fromUrl("http://sjsumelee.challonge.com/FSF84singles"))
    }

    @Test
    fun testFromUrlWithEmptyString() {
        assertNull(BracketSource.fromUrl(""))
    }

    @Test
    fun testFromUrlWithGibberish1() {
        assertNull(BracketSource.fromUrl("3ReatP2K7BbiEBaMoGcMXpklVCtDOLXa7ycPzwQ"))
    }

    @Test
    fun testFromUrlWithGibberish2() {
        assertNull(BracketSource.fromUrl("ed5T2#MO+u\"TNA3VPnG8,c(.FO^J!d~Q~G}7D%k\\I'58sJT`n_Uqi>p-E(+"))
    }

    @Test
    fun testFromUrlWithGibberish3() {
        assertNull(BracketSource.fromUrl("R`5,me_`>Hp)x:J7O"))
    }

    @Test
    fun testFromUrlWithGoogle() {
        assertNull(BracketSource.fromUrl("https://www.google.com/"))
    }

    @Test
    fun testFromUrlWithNull() {
        assertNull(BracketSource.fromUrl(null))
    }

    @Test
    fun testFromUrlWithReddit() {
        assertNull(BracketSource.fromUrl("https://www.reddit.com/"))
    }

    @Test
    fun testFromUrlWithSmashGg1() {
        assertEquals(BracketSource.SMASH_GG,
                BracketSource.fromUrl("https://smash.gg/tournament/super-south-bay-sunday-32/events/melee-singles/brackets"))
    }

    @Test
    fun testFromUrlWithSmashGg2() {
        assertEquals(BracketSource.SMASH_GG,
                BracketSource.fromUrl("https://smash.gg/tournament/gator-games-monthly-3-1/events/melee-singles/overview"))
    }

    @Test
    fun testFromUrlWithSmashGg3() {
        assertEquals(BracketSource.SMASH_GG,
                BracketSource.fromUrl("https://smash.gg/tournament/wombo-wednesday-47/events/melee-singles/brackets"))
    }

    @Test
    fun testFromUrlWithWhitespace() {
        assertNull(BracketSource.fromUrl(" "))
    }

    @Test
    fun testFromUrlWithYtmnd() {
        assertNull(BracketSource.fromUrl("http://ytmnd.com/"))
    }

}
