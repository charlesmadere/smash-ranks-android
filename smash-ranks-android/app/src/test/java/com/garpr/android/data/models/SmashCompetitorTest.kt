package com.garpr.android.data.models

import com.garpr.android.test.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SmashCompetitorTest : BaseTest() {

    companion object {
        private val SMASH_COMPETITOR_1 = SmashCompetitor(
                id = "1",
                name = "Charles",
                tag = "Charlezard"
        )

        private val SMASH_COMPETITOR_2 = SmashCompetitor(
                id = "2",
                name = "Declan",
                tag = "Imyt",
                mains = listOf(SmashCharacter.SHEIK, SmashCharacter.FALCO, SmashCharacter.FOX)
        )

        private val SMASH_COMPETITOR_3 = SmashCompetitor(
                id = "3",
                name = "Ivan",
                tag = "gaR",
                mains = emptyList()
        )

        private val SMASH_COMPETITOR_4 = SmashCompetitor(
                id = "4",
                name = "Justin",
                tag = "mikkuz",
                mains = listOf(SmashCharacter.FOX, SmashCharacter.FOX, SmashCharacter.CPTN_FALCON, null)
        )
    }

    @Test
    fun testFilteredMains1() {
        assertNull(SMASH_COMPETITOR_1.filteredMains)
    }

    @Test
    fun testFilteredMains2() {
        val filteredMains = SMASH_COMPETITOR_2.filteredMains
        assertEquals(3, filteredMains?.size)
        assertEquals(true, filteredMains?.contains(SmashCharacter.SHEIK))
        assertEquals(true, filteredMains?.contains(SmashCharacter.FALCO))
        assertEquals(true, filteredMains?.contains(SmashCharacter.FOX))
    }

    @Test
    fun testFilteredMains3() {
        assertNull(SMASH_COMPETITOR_3.filteredMains)
    }

    @Test
    fun testFilteredMains4() {
        val filteredMains = SMASH_COMPETITOR_4.filteredMains
        assertEquals(2, filteredMains?.size)
        assertEquals(true, filteredMains?.contains(SmashCharacter.FOX))
        assertEquals(true, filteredMains?.contains(SmashCharacter.CPTN_FALCON))
    }

}
