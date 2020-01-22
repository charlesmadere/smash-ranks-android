package com.garpr.android.features.setRegion

import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.RegionsBundle
import com.garpr.android.features.common.viewModels.BaseViewModelTest
import com.garpr.android.features.setRegion.SetRegionViewModel.ListItem
import com.garpr.android.features.setRegion.SetRegionViewModel.SaveIconStatus
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.RegionRepository
import com.garpr.android.repositories.RegionsRepository
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.core.inject

class SetRegionViewModelTest : BaseViewModelTest() {

    private val regionsRepository = RegionsRepositoryOverride()
    private lateinit var viewModel: SetRegionViewModel

    protected val regionRepository: RegionRepository by inject()
    protected val schedulers: Schedulers by inject()
    protected val timber: Timber by inject()

    companion object {
        private val CHICAGO = Region(
                displayName = "Chicago",
                id = "chicago",
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val GOOGLE_MTV = Region(
                displayName = "Google MTV",
                id = "googlemtv",
                endpoint = Endpoint.GAR_PR
        )

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val NEW_YORK_CITY = Region(
                displayName = "New York City",
                id = "nyc",
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val ALL_REGIONS_BUNDLE = RegionsBundle(
                regions = listOf(CHICAGO, GOOGLE_MTV, NORCAL, NEW_YORK_CITY)
        )

        private val EMPTY_REGIONS_BUNDLE = RegionsBundle()

        private val GAR_PR_REGIONS_BUNDLE = RegionsBundle(
                regions = listOf(GOOGLE_MTV, NORCAL)
        )

        private val NOT_GAR_PR_REGIONS_BUNDLE = RegionsBundle(
                regions = listOf(CHICAGO, NEW_YORK_CITY)
        )
    }

    @Before
    override fun setUp() {
        super.setUp()

        viewModel = SetRegionViewModel(regionRepository, regionsRepository, schedulers, timber)
    }

    @Test
    fun testListWithAllRegionsBundle() {
        var state: SetRegionViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        regionsRepository.regionsBundle = ALL_REGIONS_BUNDLE
        viewModel.fetchRegions()

        assertNotNull(state?.list)
        assertEquals(6, state?.list?.size)

        var endpoint = state?.list?.get(0) as ListItem.Endpoint
        assertEquals(Endpoint.GAR_PR, endpoint.endpoint)

        var region = state?.list?.get(1) as ListItem.Region
        assertEquals(GOOGLE_MTV.id, region.region.id)

        region = state?.list?.get(2) as ListItem.Region
        assertEquals(NORCAL.id, region.region.id)

        endpoint = state?.list?.get(3) as ListItem.Endpoint
        assertEquals(Endpoint.NOT_GAR_PR, endpoint.endpoint)

        region = state?.list?.get(4) as ListItem.Region
        assertEquals(CHICAGO.id, region.region.id)

        region = state?.list?.get(5) as ListItem.Region
        assertEquals(NEW_YORK_CITY.id, region.region.id)
    }

    @Test
    fun testListWithGarPrRegionsBundle() {
        var state: SetRegionViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        regionsRepository.regionsBundle = GAR_PR_REGIONS_BUNDLE
        viewModel.fetchRegions()

        assertNotNull(state?.list)
        assertEquals(3, state?.list?.size)

        val endpoint = state?.list?.get(0) as ListItem.Endpoint
        assertEquals(Endpoint.GAR_PR, endpoint.endpoint)

        var region = state?.list?.get(1) as ListItem.Region
        assertEquals(GOOGLE_MTV.id, region.region.id)

        region = state?.list?.get(2) as ListItem.Region
        assertEquals(NORCAL.id, region.region.id)
    }

    @Test
    fun testListWithNotGarPrRegionsBundle() {
        var state: SetRegionViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        regionsRepository.regionsBundle = NOT_GAR_PR_REGIONS_BUNDLE
        viewModel.fetchRegions()

        assertNotNull(state?.list)
        assertEquals(3, state?.list?.size)

        val endpoint = state?.list?.get(0) as ListItem.Endpoint
        assertEquals(Endpoint.NOT_GAR_PR, endpoint.endpoint)

        var region = state?.list?.get(1) as ListItem.Region
        assertEquals(CHICAGO.id, region.region.id)

        region = state?.list?.get(2) as ListItem.Region
        assertEquals(NEW_YORK_CITY.id, region.region.id)
    }

    @Test
    fun testSaveIconStatus() {
        var state: SetRegionViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        assertTrue(state == null || state?.saveIconStatus == SaveIconStatus.GONE)

        viewModel.fetchRegions()
        assertEquals(SaveIconStatus.DISABLED, state?.saveIconStatus)

        viewModel.selectedRegion = GOOGLE_MTV
        assertEquals(SaveIconStatus.ENABLED, state?.saveIconStatus)

        viewModel.selectedRegion = CHICAGO
        assertEquals(SaveIconStatus.ENABLED, state?.saveIconStatus)

        viewModel.selectedRegion = NORCAL
        assertEquals(SaveIconStatus.DISABLED, state?.saveIconStatus)
    }

    @Test
    fun testSaveSelectedRegion() {
        viewModel.fetchRegions()
        viewModel.selectedRegion = CHICAGO
        var throwable: Throwable? = null

        try {
            viewModel.saveSelectedRegion()
        } catch (t: Throwable) {
            throwable = t
        }

        assertNull(throwable)
        assertEquals(CHICAGO, regionRepository.region)
    }

    @Test
    fun testSaveSelectedRegionThrowsExceptionIfNoRegionSelected() {
        viewModel.fetchRegions()
        var throwable: Throwable? = null

        try {
            viewModel.saveSelectedRegion()
        } catch (t: Throwable) {
            throwable = t
        }

        assertNotNull(throwable)
    }

    @Test
    fun testStateWithEmptyRegionsBundle() {
        var state: SetRegionViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        regionsRepository.regionsBundle = EMPTY_REGIONS_BUNDLE
        viewModel.fetchRegions()

        assertEquals(true, state?.hasError)
        assertTrue(state?.list.isNullOrEmpty())
    }

    @Test
    fun testStateWithNullRegionsBundle() {
        var state: SetRegionViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        regionsRepository.regionsBundle = null
        viewModel.fetchRegions()

        assertEquals(true, state?.hasError)
        assertEquals(false, state?.isFetching)
        assertEquals(true, state?.isRefreshEnabled)
        assertNull(state?.list)
        assertNull(state?.selectedRegion)
        assertEquals(SaveIconStatus.GONE, state?.saveIconStatus)
    }

    @Test
    fun testWarnBeforeClose() {
        assertFalse(viewModel.warnBeforeClose)

        viewModel.fetchRegions()
        assertFalse(viewModel.warnBeforeClose)

        viewModel.selectedRegion = NEW_YORK_CITY
        assertTrue(viewModel.warnBeforeClose)

        viewModel.selectedRegion = regionRepository.region
        assertFalse(viewModel.warnBeforeClose)

        viewModel.selectedRegion = GOOGLE_MTV
        assertTrue(viewModel.warnBeforeClose)
    }

    private class RegionsRepositoryOverride(
            internal var regionsBundle: RegionsBundle? = ALL_REGIONS_BUNDLE
    ) : RegionsRepository {

        override fun getRegions(): Single<RegionsBundle> {
            val regionsBundle = this.regionsBundle

            return if (regionsBundle == null) {
                Single.error(NullPointerException())
            } else {
                Single.just(regionsBundle)
            }
        }

    }

}
