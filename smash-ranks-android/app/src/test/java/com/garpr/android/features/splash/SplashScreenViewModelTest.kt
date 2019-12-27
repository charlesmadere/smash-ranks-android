package com.garpr.android.features.splash

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.Region
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.RegionRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SplashScreenViewModelTest : BaseTest() {

    private lateinit var viewModel: SplashScreenViewModel

    protected val generalPreferenceStore: GeneralPreferenceStore by inject()
    protected val identityRepository: IdentityRepository by inject()
    protected val regionRepository: RegionRepository by inject()
    protected val schedulers: Schedulers by inject()
    protected val timber: Timber by inject()

    companion object {
        private val CHARLEZARD: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
        )

        private val IMYT: AbsPlayer = LitePlayer(
                id = "5877eb55d2994e15c7dea98b",
                name = "Imyt"
        )

        private val ATLANTA = Region(
                displayName = "Atlanta",
                id = "atlanta",
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )
    }

    @Before
    override fun setUp() {
        super.setUp()

        viewModel = SplashScreenViewModel(generalPreferenceStore, identityRepository,
                regionRepository, schedulers, timber)
    }

    @Test
    fun testCustomizeIdentity() {
        var state: SplashScreenViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        assertNotNull(state)
        assertEquals(false, state?.isSplashScreenComplete)
        assertNull(state?.identity)
        assertEquals(regionRepository.getRegion(), state?.region)

        identityRepository.setIdentity(CHARLEZARD, NORCAL)
        assertNotNull(state)
        assertEquals(false, state?.isSplashScreenComplete)
        assertEquals(CHARLEZARD, state?.identity)
        assertEquals(regionRepository.getRegion(), state?.region)
    }

    @Test
    fun testCustomizeIdentityAndRegionAndSetSplashScreenComplete() {
        var state: SplashScreenViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        assertNotNull(state)
        assertEquals(false, state?.isSplashScreenComplete)
        assertNull(state?.identity)
        assertEquals(regionRepository.getRegion(), state?.region)

        identityRepository.setIdentity(IMYT, NORCAL)
        assertNotNull(state)
        assertEquals(false, state?.isSplashScreenComplete)
        assertEquals(IMYT, state?.identity)
        assertEquals(regionRepository.getRegion(), state?.region)

        regionRepository.setRegion(ATLANTA)
        assertNotNull(state)
        assertEquals(false, state?.isSplashScreenComplete)
        assertEquals(IMYT, state?.identity)
        assertEquals(ATLANTA, state?.region)

        viewModel.setSplashScreenComplete()
        assertNotNull(state)
        assertEquals(true, state?.isSplashScreenComplete)
        assertEquals(IMYT, state?.identity)
        assertEquals(ATLANTA, state?.region)
    }

    @Test
    fun testCustomizeRegion() {
        var state: SplashScreenViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        assertNotNull(state)
        assertEquals(false, state?.isSplashScreenComplete)
        assertNull(state?.identity)
        assertEquals(regionRepository.getRegion(), state?.region)

        regionRepository.setRegion(ATLANTA)
        assertNotNull(state)
        assertEquals(false, state?.isSplashScreenComplete)
        assertNull(state?.identity)
        assertEquals(ATLANTA, state?.region)
    }

    @Test
    fun testIsSplashScreenCompleteOnFirstLaunch() {
        var state: SplashScreenViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        assertNotNull(state)
        assertEquals(false, state?.isSplashScreenComplete)
    }

    @Test
    fun testSetSplashScreenComplete() {
        var state: SplashScreenViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        assertNotNull(state)
        assertEquals(false, state?.isSplashScreenComplete)
        assertNull(state?.identity)
        assertEquals(regionRepository.getRegion(), state?.region)

        viewModel.setSplashScreenComplete()
        assertNotNull(state)
        assertEquals(true, state?.isSplashScreenComplete)
        assertNull(state?.identity)
        assertEquals(regionRepository.getRegion(), state?.region)
    }

}
