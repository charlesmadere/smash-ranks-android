package com.garpr.android.features.tournaments

import com.garpr.android.BaseViewModelTest
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.LiteTournament
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.data.models.TournamentsBundle
import com.garpr.android.features.tournaments.TournamentsViewModel.ListItem
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.TournamentsRepository
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.koin.test.inject
import java.util.Calendar

class TournamentsViewModelTest : BaseViewModelTest() {

    private val tournamentsRepository = TournamentsRepositoryOverride()
    private lateinit var viewModel: TournamentsViewModel

    protected val schedulers: Schedulers by inject()
    protected val threadUtils: ThreadUtils by inject()
    protected val timber: Timber by inject()

    companion object {
        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val GENESIS_RED: AbsTournament = LiteTournament(
                date = SimpleDate(with(Calendar.getInstance()) {
                    clear()
                    set(Calendar.YEAR, 2017)
                    set(Calendar.MONTH, Calendar.JULY)
                    set(Calendar.DAY_OF_MONTH, 9)
                    time
                }),
                regions = listOf(NORCAL.id),
                id = "597d2903d2994e34028b4cc4",
                name = "GENESIS: RED"
        )

        private val GET_MADE_AT_THE_FOUNDRY: AbsTournament = LiteTournament(
                date = SimpleDate(with(Calendar.getInstance()) {
                    clear()
                    set(Calendar.YEAR, 2017)
                    set(Calendar.MONTH, Calendar.FEBRUARY)
                    set(Calendar.DAY_OF_MONTH, 21)
                    time
                }),
                regions = listOf(NORCAL.id),
                id = "58ad3b1cd2994e756952adba",
                name = "Get MADE at the Foundry"
        )

        private val THE_BEAT_DOWN_14: AbsTournament = LiteTournament(
                date = SimpleDate(with(Calendar.getInstance()) {
                    clear()
                    set(Calendar.YEAR, 2017)
                    set(Calendar.MONTH, Calendar.JANUARY)
                    set(Calendar.DAY_OF_MONTH, 5)
                    time
                }),
                regions = listOf(NORCAL.id),
                id = "588827bad2994e0d53b14556",
                name = "The Beat Down Ep.14"
        )

        private val EMPTY_TOURNAMENTS_BUNDLE = TournamentsBundle()

        private val TOURNAMENTS_BUNDLE = TournamentsBundle(
                tournaments = listOf(GENESIS_RED, GET_MADE_AT_THE_FOUNDRY, THE_BEAT_DOWN_14)
        )
    }

    @Before
    override fun setUp() {
        super.setUp()

        viewModel = TournamentsViewModel(schedulers, threadUtils, timber, tournamentsRepository)
    }

    @Test
    fun testFetchTournamentsBundle() {
        var state: TournamentsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchTournaments(NORCAL)
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertEquals(3, state?.list?.size)
        assertNull(state?.searchResults)
        assertEquals(TOURNAMENTS_BUNDLE, state?.tournamentsBundle)

        var listItem = state?.list?.get(0) as ListItem.Tournament
        assertEquals(GENESIS_RED, listItem.tournament)

        listItem = state?.list?.get(1) as ListItem.Tournament
        assertEquals(GET_MADE_AT_THE_FOUNDRY, listItem.tournament)

        listItem = state?.list?.get(2) as ListItem.Tournament
        assertEquals(THE_BEAT_DOWN_14, listItem.tournament)
    }

    @Test
    fun testFetchEmptyTournamentsBundle() {
        tournamentsRepository.tournamentsBundle = EMPTY_TOURNAMENTS_BUNDLE

        var state: TournamentsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchTournaments(NORCAL)
        assertEquals(false, state?.hasError)
        assertEquals(true, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertNull(state?.list)
        assertNull(state?.searchResults)
        assertEquals(EMPTY_TOURNAMENTS_BUNDLE, state?.tournamentsBundle)
    }

    @Test
    fun testSearchWithGe() {
        var state: TournamentsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchTournaments(NORCAL)
        viewModel.search("ge")
        assertEquals(2, state?.searchResults?.size)

        var listItem = state?.searchResults?.get(0) as ListItem.Tournament
        assertEquals(GENESIS_RED, listItem.tournament)

        listItem = state?.searchResults?.get(1) as ListItem.Tournament
        assertEquals(GET_MADE_AT_THE_FOUNDRY, listItem.tournament)
    }

    @Test
    fun testSearchWithEmptyString() {
        var state: TournamentsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchTournaments(NORCAL)
        viewModel.search("")
        assertNull(state?.searchResults)
    }

    @Test
    fun testSearchWithNull() {
        var state: TournamentsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchTournaments(NORCAL)
        viewModel.search(null)
        assertNull(state?.searchResults)
    }

    @Test
    fun testSearchWithWhiteSpace() {
        var state: TournamentsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchTournaments(NORCAL)
        viewModel.search(" ")
        assertNull(state?.searchResults)
    }

    @Test
    fun testSearchWithXyz() {
        var state: TournamentsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchTournaments(NORCAL)
        viewModel.search("XyZ")
        assertEquals(1, state?.searchResults?.size)

        val listItem = state?.searchResults?.get(0) as ListItem.NoResults
        assertEquals("XyZ", listItem.query)
    }

    private class TournamentsRepositoryOverride(
            internal var tournamentsBundle: TournamentsBundle? = TOURNAMENTS_BUNDLE
    ) : TournamentsRepository {
        override fun getTournament(region: Region, tournamentId: String): Single<FullTournament> {
            throw NotImplementedError()
        }

        override fun getTournaments(region: Region): Single<TournamentsBundle> {
            val tournamentsBundle = this.tournamentsBundle

            return if (tournamentsBundle == null) {
                Single.error(NullPointerException())
            } else {
                Single.just(tournamentsBundle)
            }
        }
    }

}
