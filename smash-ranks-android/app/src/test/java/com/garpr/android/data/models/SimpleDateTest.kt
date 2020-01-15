package com.garpr.android.data.models

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Collections
import java.util.Date

class SimpleDateTest {

    @Test
    fun testChronologicalOrder() {
        val list = listOf(SimpleDate(Date(2)), SimpleDate(Date(0)),
                SimpleDate(Date(1)), SimpleDate(Date(5)), SimpleDate(Date(20)))

        Collections.sort(list, SimpleDate.CHRONOLOGICAL_ORDER)
        assertEquals(0, list[0].date.time)
        assertEquals(1, list[1].date.time)
        assertEquals(2, list[2].date.time)
        assertEquals(5, list[3].date.time)
        assertEquals(20, list[4].date.time)
    }

    @Test
    fun testReverseChronologicalOrder() {
        val list = listOf(SimpleDate(Date(2)), SimpleDate(Date(0)),
                SimpleDate(Date(1)), SimpleDate(Date(5)), SimpleDate(Date(20)))

        Collections.sort(list, SimpleDate.REVERSE_CHRONOLOGICAL_ORDER)
        assertEquals(20, list[0].date.time)
        assertEquals(5, list[1].date.time)
        assertEquals(2, list[2].date.time)
        assertEquals(1, list[3].date.time)
        assertEquals(0, list[4].date.time)
    }

}
