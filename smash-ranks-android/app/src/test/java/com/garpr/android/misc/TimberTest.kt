package com.garpr.android.misc

import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class TimberTest : BaseTest() {

    @Inject
    protected lateinit var timber: Timber


    companion object {
        private const val TAG = "TimberTest"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

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

}
