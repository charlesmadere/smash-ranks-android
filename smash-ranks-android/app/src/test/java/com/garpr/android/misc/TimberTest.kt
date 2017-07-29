package com.garpr.android.misc

import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class TimberTest : BaseTest() {

    @Inject
    lateinit protected var mTimber: Timber


    companion object {
        private const val TAG = "TimberTest"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun testClearEntries() {
        mTimber.clearEntries()

        var entries = mTimber.entries
        assertTrue(entries.isEmpty())

        mTimber.d(TAG, "blah")
        mTimber.clearEntries()
        entries = mTimber.entries
        assertTrue(entries.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testGetEntries() {
        val entries = mTimber.entries
        assertTrue(entries.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testGetEntriesWithOne() {
        mTimber.d(TAG, "one")
        val entries = mTimber.entries
        assertEquals(1, entries.size)
    }

    @Test
    @Throws(Exception::class)
    fun testGetEntriesWithTwo() {
        mTimber.d(TAG, "one")
        mTimber.w(TAG, "two")
        val entries = mTimber.entries
        assertEquals(2, entries.size)
    }

    @Test
    @Throws(Exception::class)
    fun testGetEntriesWithThree() {
        mTimber.d(TAG, "one")
        mTimber.w(TAG, "two")
        mTimber.e(TAG, "three")
        val entries = mTimber.entries
        assertEquals(3, entries.size)
    }

}
