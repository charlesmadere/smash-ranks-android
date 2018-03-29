package com.garpr.android.models

import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BracketSourceTest : BaseTest() {

    @Test
    @Throws(Exception::class)
    fun testFromUrlWithBing() {
        assertNull(BracketSource.fromUrl("https://www.bing.com/"))
    }

    @Test
    @Throws(Exception::class)
    fun testFromUrlWithChallonge1() {
        assertEquals(BracketSource.CHALLONGE,
                BracketSource.fromUrl("http://challonge.com/stanfordmelee7"))
    }

    @Test
    @Throws(Exception::class)
    fun testFromUrlWithChallonge2() {
        assertEquals(BracketSource.CHALLONGE,
                BracketSource.fromUrl("http://afkgg.challonge.com/afk17"))
    }

    @Test
    @Throws(Exception::class)
    fun testFromUrlWithChallonge3() {
        assertEquals(BracketSource.CHALLONGE,
                BracketSource.fromUrl("http://sjsumelee.challonge.com/FSF84singles"))
    }

    @Test
    @Throws(Exception::class)
    fun testFromUrlWithEmptyString() {
        assertNull(BracketSource.fromUrl(""))
    }

    @Test
    @Throws(Exception::class)
    fun testFromUrlWithGibberish1() {
        assertNull(BracketSource.fromUrl("3ReatP2K7BbiEBaMoGcMXpklVCtDOLXa7ycPzwQ"))
    }

    @Test
    @Throws(Exception::class)
    fun testFromUrlWithGibberish2() {
        assertNull(BracketSource.fromUrl("ed5T2#MO+u\"TNA3VPnG8,c(.FO^J!d~Q~G}7D%k\\I'58sJT`n_Uqi>p-E(+"))
    }

    @Test
    @Throws(Exception::class)
    fun testFromUrlWithGibberish3() {
        assertNull(BracketSource.fromUrl("R`5,me_`>Hp)x:J7O"))
    }

    @Test
    @Throws(Exception::class)
    fun testFromUrlWithGoogle() {
        assertNull(BracketSource.fromUrl("https://www.google.com/"))
    }

    @Test
    @Throws(Exception::class)
    fun testFromUrlWithNull() {
        assertNull(BracketSource.fromUrl(null))
    }

    @Test
    @Throws(Exception::class)
    fun testFromUrlWithReddit() {
        assertNull(BracketSource.fromUrl("https://www.reddit.com/"))
    }

    @Test
    @Throws(Exception::class)
    fun testFromUrlWithSmashGg1() {
        assertEquals(BracketSource.SMASH_GG,
                BracketSource.fromUrl("https://smash.gg/tournament/super-south-bay-sunday-32/events/melee-singles/brackets"))
    }

    @Test
    @Throws(Exception::class)
    fun testFromUrlWithSmashGg2() {
        assertEquals(BracketSource.SMASH_GG,
                BracketSource.fromUrl("https://smash.gg/tournament/gator-games-monthly-3-1/events/melee-singles/overview"))
    }

    @Test
    @Throws(Exception::class)
    fun testFromUrlWithSmashGg3() {
        assertEquals(BracketSource.SMASH_GG,
                BracketSource.fromUrl("https://smash.gg/tournament/wombo-wednesday-47/events/melee-singles/brackets"))
    }

    @Test
    @Throws(Exception::class)
    fun testFromUrlWithWhitespace() {
        assertNull(BracketSource.fromUrl(" "))
    }

    @Test
    @Throws(Exception::class)
    fun testFromUrlWithYtmnd() {
        assertNull(BracketSource.fromUrl("http://ytmnd.com/"))
    }

}
