package com.garpr.android.data.models

import com.garpr.android.test.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.Collections
import java.util.Date

class SimpleDateTest : BaseTest() {

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
    fun testHashCodeWithChristmas() {
        val date = with(Calendar.getInstance()) {
            clear()
            set(Calendar.YEAR, 2017)
            set(Calendar.MONTH, Calendar.DECEMBER)
            set(Calendar.DAY_OF_MONTH, 25)
            time
        }

        val simpleDate = SimpleDate(date)
        assertEquals(date.hashCode(), simpleDate.hashCode())
    }

    @Test
    fun testHashCodeWithGenesis7() {
        val date = with(Calendar.getInstance()) {
            clear()
            set(Calendar.YEAR, 2020)
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DAY_OF_MONTH, 24)
            time
        }

        val simpleDate = SimpleDate(date)
        assertEquals(date.hashCode(), simpleDate.hashCode())
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
