package com.garpr.android.data.models

import android.os.Parcel
import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Collections
import java.util.Date

@RunWith(RobolectricTestRunner::class)
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
    fun testParcelable() {
        val simpleDate = SimpleDate()
        val parcel = Parcel.obtain()
        simpleDate.writeToParcel(parcel, simpleDate.describeContents())
        parcel.setDataPosition(0)

        val simpleDateFromParcel = SimpleDate.CREATOR.createFromParcel(parcel)
        assertEquals(simpleDate, simpleDateFromParcel)
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
