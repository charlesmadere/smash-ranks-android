package com.garpr.android.models

import com.garpr.android.BaseTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FullPlayerTest : BaseTest() {

    companion object {
        private val FULL_PLAYER_1 = FullPlayer("1", "Imyt",
                listOf("imyt", "IMYT", "Dr. Mario"), null, null)
        private val FULL_PLAYER_2 = FullPlayer("2", "Charlezard",
                listOf("charlezard"), null, null)
        private val FULL_PLAYER_3 = FullPlayer("3", "gaR",
                listOf("retired", "RETIRED", "ripgarpr", "ivan"), null, null)
        private val FULL_PLAYER_4 = FullPlayer("4", "mikkuz",
                listOf(), null, null)
        private val FULL_PLAYER_5 = FullPlayer("5", "grandma Dan",
                null, null, null)
    }

    @Test
    @Throws(Exception::class)
    fun testUniqueAliasesWithFullPlayer1() {
        val uniqueAliases = FULL_PLAYER_1.uniqueAliases
        assertTrue(uniqueAliases != null && uniqueAliases.size == 1)
    }

    @Test
    @Throws(Exception::class)
    fun testUniqueAliasesWithFullPlayer2() {
        val uniqueAliases = FULL_PLAYER_2.uniqueAliases
        assertTrue(uniqueAliases == null || uniqueAliases.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testUniqueAliasesWithFullPlayer3() {
        val uniqueAliases = FULL_PLAYER_3.uniqueAliases
        assertTrue(uniqueAliases != null && uniqueAliases.size == 3)
    }

    @Test
    @Throws(Exception::class)
    fun testUniqueAliasesWithFullPlayer4() {
        val uniqueAliases = FULL_PLAYER_4.uniqueAliases
        assertTrue(uniqueAliases == null || uniqueAliases.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testUniqueAliasesWithFullPlayer5() {
        val uniqueAliases = FULL_PLAYER_5.uniqueAliases
        assertTrue(uniqueAliases == null || uniqueAliases.isEmpty())
    }

}
