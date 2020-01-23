package com.garpr.android.repositories

import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.LiteTournament
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.data.models.TournamentsBundle
import com.garpr.android.networking.AbsServerApi
import com.garpr.android.test.BaseTest
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Date

class TournamentsRepositoryTest : BaseTest() {

    private val serverApi = ServerApiOverride()
    private lateinit var tournamentsRepository: TournamentsRepository

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

        private val BLARGH = LitePlayer(
                id = "58885df9d2994e3d56594112",
                name = "blargh"
        )

        private val NMW = LitePlayer(
                id = "583a4a15d2994e0577b05c8a",
                name = "NMW"
        )

        private val SNAP = LitePlayer(
                id = "59213f1ad2994e1d79144956",
                name = "Snap"
        )

        private val THE_UNABLE_TABLE = LitePlayer(
                id = "58b132d5d2994e7265472773",
                name = "theunabletable"
        )

        private val UMARTH = LitePlayer(
                id = "5877eb55d2994e15c7dea977",
                name = "Umarth"
        )

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val FULL_TOURNAMENT = FullTournament(
                date = SimpleDate(Date(1577433600000L)),
                id = "5e071549d2994e083b9c1466",
                name = "Melee @ the Made #123",
                players = listOf(NMW, BLARGH, UMARTH, THE_UNABLE_TABLE, SNAP)
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

        tournamentsRepository = TournamentsRepositoryImpl(serverApi)
    }

    @Test
    fun testGetTournament() {
        val fullTournament = tournamentsRepository.getTournament(NORCAL, FULL_TOURNAMENT.id)
                .blockingGet()

        assertNotNull(fullTournament)
        assertEquals(5, fullTournament.players?.size)
        assertEquals(BLARGH, fullTournament.players?.get(0))
        assertEquals(NMW, fullTournament.players?.get(1))
        assertEquals(SNAP, fullTournament.players?.get(2))
        assertEquals(THE_UNABLE_TABLE, fullTournament.players?.get(3))
        assertEquals(UMARTH, fullTournament.players?.get(4))
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
            internal var fullTournament: FullTournament? = FULL_TOURNAMENT,
            internal var tournamentsBundle: TournamentsBundle? = TOURNAMENTS_BUNDLE
    ) : AbsServerApi() {
        override fun getTournament(region: Region, tournamentId: String): Single<FullTournament> {
            val fullTournament = this.fullTournament

            return if (fullTournament == null) {
                Single.error(NullPointerException())
            } else {
                Single.just(fullTournament)
            }
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
