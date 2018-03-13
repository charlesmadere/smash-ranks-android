package com.garpr.android.misc

import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class TournamentAdapterManagerTest : BaseTest() {

    @Inject
    protected lateinit var tournamentAdapterManager: TournamentAdapterManager


    companion object {
        private const val EMPTY = ""
        private const val HELLO_WORLD = "Hello, World!"
        private const val WHITESPACE = " "
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun testGetItemIdWithString() {
        assertEquals(EMPTY.hashCode(), tournamentAdapterManager.getItemId(1, EMPTY))
        assertEquals(HELLO_WORLD.hashCode(), tournamentAdapterManager.getItemId(1, HELLO_WORLD))
        assertEquals(WHITESPACE.hashCode(), tournamentAdapterManager.getItemId(1, WHITESPACE))
    }

    @Test
    @Throws(Exception::class)
    fun testGetItemViewTypeWithString() {
        assertEquals(TournamentAdapterManager.ViewType.MESSAGE,
                tournamentAdapterManager.getItemViewType(1, EMPTY))
        assertEquals(TournamentAdapterManager.ViewType.MESSAGE,
                tournamentAdapterManager.getItemViewType(1, HELLO_WORLD))
        assertEquals(TournamentAdapterManager.ViewType.MESSAGE,
                tournamentAdapterManager.getItemViewType(1, WHITESPACE))
    }

}
