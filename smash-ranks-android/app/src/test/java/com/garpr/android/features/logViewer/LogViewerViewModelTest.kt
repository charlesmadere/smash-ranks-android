package com.garpr.android.features.logViewer

import com.garpr.android.BaseTest
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LogViewerViewModelTest : BaseTest() {

    private lateinit var viewModel: LogViewerViewModel

    protected val threadUtils: ThreadUtils by inject()
    protected val timber: Timber by inject()

    companion object {
        private const val DEBUG = "DEBUG"
        private const val ERROR = "ERROR"
        private const val WARN = "WARN"
    }

    @Before
    override fun setUp() {
        super.setUp()

        viewModel = LogViewerViewModel(threadUtils, timber)
    }

    @Test
    fun testClearEntries() {
        timber.d(DEBUG, "one")
        timber.d(WARN, "two")
        timber.d(ERROR, "three")

        var state: LogViewerViewModel.State? = null
        val isFetchingStates = mutableListOf<Boolean>()

        viewModel.stateLiveData.observeForever {
            state = it
            isFetchingStates.add(it.isFetching)
        }

        viewModel.clearEntries()
        assertEquals(true, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertTrue(state?.entries.isNullOrEmpty())
        assertEquals(2, isFetchingStates.size)
        assertTrue(isFetchingStates[0])
        assertFalse(isFetchingStates[1])
    }

    @Test
    fun testClearEntriesWithEmptyTimber() {
        var state: LogViewerViewModel.State? = null
        val isFetchingStates = mutableListOf<Boolean>()

        viewModel.stateLiveData.observeForever {
            state = it
            isFetchingStates.add(it.isFetching)
        }

        viewModel.clearEntries()
        assertEquals(true, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertTrue(state?.entries.isNullOrEmpty())
        assertEquals(2, isFetchingStates.size)
        assertTrue(isFetchingStates[0])
        assertFalse(isFetchingStates[1])
    }

    @Test
    fun testFetchEntries() {
        timber.d(DEBUG, "one")
        timber.d(WARN, "two")
        timber.d(ERROR, "three")

        var state: LogViewerViewModel.State? = null
        val isFetchingStates = mutableListOf<Boolean>()

        viewModel.stateLiveData.observeForever {
            state = it
            isFetchingStates.add(it.isFetching)
        }

        viewModel.fetchEntries()
        assertEquals(false, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertEquals(3, state?.entries?.size)
        assertEquals(2, isFetchingStates.size)
        assertTrue(isFetchingStates[0])
        assertFalse(isFetchingStates[1])
    }

    @Test
    fun testFetchEntriesAndClearEntries() {
        timber.d(DEBUG, "one")
        timber.d(WARN, "two")
        timber.d(ERROR, "three")

        var state: LogViewerViewModel.State? = null
        val isFetchingStates = mutableListOf<Boolean>()

        viewModel.stateLiveData.observeForever {
            state = it
            isFetchingStates.add(it.isFetching)
        }

        viewModel.fetchEntries()
        assertEquals(false, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertNotNull(state?.entries)
        assertEquals(3, state?.entries?.size)
        assertEquals(2, isFetchingStates.size)
        assertTrue(isFetchingStates[0])
        assertFalse(isFetchingStates[1])

        viewModel.clearEntries()
        assertEquals(true, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertTrue(state?.entries.isNullOrEmpty())
        assertEquals(4, isFetchingStates.size)
        assertTrue(isFetchingStates[2])
        assertFalse(isFetchingStates[3])
    }

    @Test
    fun testFetchEntriesWithEmptyTimber() {
        var state: LogViewerViewModel.State? = null
        val isFetchingStates = mutableListOf<Boolean>()

        viewModel.stateLiveData.observeForever {
            state = it
            isFetchingStates.add(it.isFetching)
        }

        viewModel.fetchEntries()
        assertEquals(true, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertTrue(state?.entries.isNullOrEmpty())
        assertEquals(2, isFetchingStates.size)
        assertTrue(isFetchingStates[0])
        assertFalse(isFetchingStates[1])
    }

}
