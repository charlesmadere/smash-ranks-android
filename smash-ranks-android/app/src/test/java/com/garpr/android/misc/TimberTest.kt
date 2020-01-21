package com.garpr.android.misc

import com.garpr.android.test.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.koin.test.inject
import kotlin.random.Random

class TimberTest : BaseTest() {

    protected val timber: Timber by inject()

    @Test
    fun testClearEntries() {
        timber.clearEntries()

        var entries = timber.entries
        assertTrue(entries.isEmpty())

        timber.d(TAG, "blah")
        timber.clearEntries()
        entries = timber.entries
        assertTrue(entries.isEmpty())
    }

    @Test
    fun testGetEntries() {
        val entries = timber.entries
        assertTrue(entries.isEmpty())
    }

    @Test
    fun testGetEntriesWithOne() {
        timber.d(TAG, "one")
        val entries = timber.entries
        assertEquals(1, entries.size)
    }

    @Test
    fun testGetEntriesWithTwo() {
        timber.d(TAG, "one")
        timber.w(TAG, "two")
        val entries = timber.entries
        assertEquals(2, entries.size)
    }

    @Test
    fun testGetEntriesWithThree() {
        timber.d(TAG, "one")
        timber.w(TAG, "two")
        timber.e(TAG, "three")
        val entries = timber.entries
        assertEquals(3, entries.size)
    }

    @Test
    fun testMaxSize() {
        val times = Random.nextInt(1000, 2000)

        repeat(times) {
            timber.d(TAG, it.toString())
        }

        val entries = timber.entries
        assertNotEquals(times, entries.size)
    }

    companion object {
        private const val TAG = "TimberTest"
    }

}
