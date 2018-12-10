package com.garpr.android.models

import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
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
        assertTrue(filteredMains?.contains(SmashCharacter.SHEIK) == true)
        assertTrue(filteredMains?.contains(SmashCharacter.FALCO) == true)
        assertTrue(filteredMains?.contains(SmashCharacter.FOX) == true)
    }

    @Test
    fun testFilteredMains3() {
        assertNull(SMASH_COMPETITOR_3.filteredMains)
    }

    @Test
    fun testFilteredMains4() {
        val filteredMains = SMASH_COMPETITOR_4.filteredMains
        assertEquals(2, filteredMains?.size)
        assertTrue(filteredMains?.contains(SmashCharacter.FOX) == true)
        assertTrue(filteredMains?.contains(SmashCharacter.CPTN_FALCON) == true)
    }

}
