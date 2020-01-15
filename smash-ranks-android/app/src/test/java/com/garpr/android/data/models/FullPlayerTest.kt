package com.garpr.android.data.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FullPlayerTest {

    companion object {
        private val FULL_PLAYER_1 = FullPlayer(
                id = "1",
                name = "Imyt",
                aliases = listOf("imyt", "IMYT", "Dr. Mario")
        )

        private val FULL_PLAYER_2 = FullPlayer(
                id = "2",
                name = "Charlezard",
                aliases = listOf("charlezard")
        )

        private val FULL_PLAYER_3 = FullPlayer(
                id = "3",
                name = "gaR",
                aliases = listOf("retired", "RETIRED", "ripgarpr", "ivan")
        )

        private val FULL_PLAYER_4 = FullPlayer(
                id = "4",
                name = "mikkuz",
                aliases = emptyList()
        )

        private val FULL_PLAYER_5 = FullPlayer(
                id = "5",
                name = "grandma Dan"
        )

        private val FULL_PLAYER_6 = FullPlayer(
                id = "6",
                name = "Oatsngoats",
                aliases = listOf("")
        )
    }

    @Test
    fun testUniqueAliasesWithFullPlayer1() {
        val uniqueAliases = FULL_PLAYER_1.uniqueAliases
        assertNotNull(uniqueAliases)
        assertEquals(1, uniqueAliases?.size)
    }

    @Test
    fun testUniqueAliasesWithFullPlayer2() {
        val uniqueAliases = FULL_PLAYER_2.uniqueAliases
        assertTrue(uniqueAliases.isNullOrEmpty())
    }

    @Test
    fun testUniqueAliasesWithFullPlayer3() {
        val uniqueAliases = FULL_PLAYER_3.uniqueAliases
        assertNotNull(uniqueAliases)
        assertEquals(3, uniqueAliases?.size)
    }

    @Test
    fun testUniqueAliasesWithFullPlayer4() {
        val uniqueAliases = FULL_PLAYER_4.uniqueAliases
        assertTrue(uniqueAliases.isNullOrEmpty())
    }

    @Test
    fun testUniqueAliasesWithFullPlayer5() {
        val uniqueAliases = FULL_PLAYER_5.uniqueAliases
        assertTrue(uniqueAliases.isNullOrEmpty())
    }

    @Test
    fun testUniqueAliasesWithFullPlayer6() {
        val uniqueAliases = FULL_PLAYER_6.uniqueAliases
        assertTrue(uniqueAliases.isNullOrEmpty())
    }

}
