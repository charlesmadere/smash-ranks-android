package com.garpr.android.repositories

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.LiteTournament
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.data.models.TournamentsBundle
import com.garpr.android.misc.Schedulers
import com.garpr.android.networking.AbsServerApi
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import java.util.Date

@RunWith(RobolectricTestRunner::class)
class TournamentsRepositoryTest : BaseTest() {

    private val serverApi = ServerApiOverride()
    private lateinit var tournamentsRepository: TournamentsRepository

    protected val schedulers: Schedulers by inject()

    companion object {
        private val TOURNAMENT_MADE_112: AbsTournament = LiteTournament(
                date = SimpleDate(Date(1567148400000L)),
                id = "5d6a1784d2994e1a9c3dd665",
                name = "Melee @ the Made #112"
        )

        private val TOURNAMENT_PEOPLES_TUESDAYS_14: AbsTournament = LiteTournament(
                date = SimpleDate(Date(1565679600000L)),
                id = "5d53bd4ed2994e22acc95a92",
                name = "The People's Tuesdays #14"
        )

        private val TOURNAMENT_PEOPLES_TUESDAYS_16: AbsTournament = LiteTournament(
                date = SimpleDate(Date(1566889200000L)),
                id = "5d661e33d2994e1a9c3dd64f",
                name = "The People's Tuesdays #16"
        )

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val EMPTY_TOURNAMENTS_BUNDLE = TournamentsBundle()

        private val TOURNAMENTS_BUNDLE = TournamentsBundle(
                tournaments = listOf(TOURNAMENT_PEOPLES_TUESDAYS_14,
                        TOURNAMENT_PEOPLES_TUESDAYS_16, TOURNAMENT_MADE_112)
        )
    }

    @Before
    override fun setUp() {
        super.setUp()

        tournamentsRepository = TournamentsRepositoryImpl(schedulers, serverApi)
    }

    @Test
    fun testGetTournaments() {
        val tournamentsBundle = tournamentsRepository.getTournaments(NORCAL)
                .blockingGet()

        assertNotNull(tournamentsBundle)
        assertEquals(3, tournamentsBundle.tournaments?.size)
        assertEquals(TOURNAMENT_MADE_112, tournamentsBundle.tournaments?.get(0))
        assertEquals(TOURNAMENT_PEOPLES_TUESDAYS_16, tournamentsBundle.tournaments?.get(1))
        assertEquals(TOURNAMENT_PEOPLES_TUESDAYS_14, tournamentsBundle.tournaments?.get(2))
    }

    @Test
    fun testGetTournamentsWithEmptyTournamentsBundle() {
        serverApi.tournamentsBundle = EMPTY_TOURNAMENTS_BUNDLE
        val tournamentsBundle = tournamentsRepository.getTournaments(NORCAL)
                .blockingGet()

        assertNotNull(tournamentsBundle)
        assertTrue(tournamentsBundle.tournaments.isNullOrEmpty())
    }

    private class ServerApiOverride(
            internal var tournamentsBundle: TournamentsBundle? = TOURNAMENTS_BUNDLE
    ) : AbsServerApi() {
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
