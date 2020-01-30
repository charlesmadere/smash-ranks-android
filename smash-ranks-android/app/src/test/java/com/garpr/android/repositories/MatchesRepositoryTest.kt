package com.garpr.android.repositories

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.LiteTournament
import com.garpr.android.data.models.MatchResult
import com.garpr.android.data.models.MatchesBundle
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.data.models.TournamentMatch
import com.garpr.android.networking.AbsServerApi
import com.garpr.android.test.BaseTest
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class MatchesRepositoryTest : BaseTest() {

    private lateinit var matchesRepository: MatchesRepository
    private val serverApi = ServerApiOverride()

    @Before
    override fun setUp() {
        super.setUp()

        matchesRepository = MatchesRepositoryImpl(serverApi)
    }

    @Test
    fun testGetMatchesWithEmptyMatchesBundle() {
        serverApi.matchesBundle = EMPTY_MATCHES_BUNDLE
        val bundle = matchesRepository.getMatches(NORCAL, CHARLEZARD.id)
                .blockingGet()

        assertTrue(bundle.matches.isNullOrEmpty())
    }

    @Test
    fun testGetMatchesWithCharlezardMatchesBundle() {
        serverApi.matchesBundle = CHARLEZARD_MATCHES_BUNDLE
        val bundle = matchesRepository.getMatches(NORCAL, CHARLEZARD.id)
                .blockingGet()

        assertEquals(5, bundle.matches?.size)
        assertEquals(MELEE_AT_THE_MADE_125, bundle?.matches?.get(0)?.tournament)
        assertEquals(MELEE_AT_THE_MADE_125, bundle?.matches?.get(1)?.tournament)
        assertEquals(MELEE_AT_THE_MADE_125, bundle?.matches?.get(2)?.tournament)
        assertEquals(NORCAL_VALIDATED_1, bundle?.matches?.get(3)?.tournament)
        assertEquals(NORCAL_VALIDATED_1, bundle?.matches?.get(4)?.tournament)
    }

    companion object {
        private val CHARLEZARD: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
        )

        private val DIMSUM: AbsPlayer = LitePlayer(
                id = "588999c5d2994e713ad63b35",
                name = "dimsum"
        )

        private val GRANDMA_DAN: AbsPlayer = LitePlayer(
                id = "588999c5d2994e713ad63a05",
                name = "grandma Dan"
        )

        private val NMW: AbsPlayer = LitePlayer(
                id = "583a4a15d2994e0577b05c8a",
                name = "NMW"
        )

        private val RAGTIME_MOUSE: AbsPlayer = LitePlayer(
                id = "588852e8d2994e3bbfa52dce",
                name = "Ragtime Mouse"
        )

        private val SNAP: AbsPlayer = LitePlayer(
                id = "59213f1ad2994e1d79144956",
                name = "Snap"
        )

        private val SPACE_PIGEON: AbsPlayer = LitePlayer(
                id = "587894e9d2994e15c7dea9c7",
                name = "SpacePigeon"
        )

        private val MELEE_AT_THE_MADE_125: AbsTournament = LiteTournament(
                date = SimpleDate(with(Calendar.getInstance()) {
                    clear()
                    set(Calendar.YEAR, 2020)
                    set(Calendar.MONTH, Calendar.JANUARY)
                    set(Calendar.DAY_OF_MONTH, 17)
                    time
                }),
                id = "5e22da86d2994e6bf4d676da",
                name = "Melee @ the Made #125"
        )

        private val NORCAL_VALIDATED_1: AbsTournament = LiteTournament(
                date = SimpleDate(with(Calendar.getInstance()) {
                    clear()
                    set(Calendar.YEAR, 2017)
                    set(Calendar.MONTH, Calendar.JANUARY)
                    set(Calendar.DAY_OF_MONTH, 14)
                    time
                }),
                id = "588850d5d2994e3bbfa52d67",
                name = "Norcal Validated 1"
        )

        private val CHARLEZARD_MATCHES_BUNDLE = MatchesBundle(
                player = CHARLEZARD,
                losses = 4,
                wins = 1,
                matches = listOf(
                        TournamentMatch(
                                opponent = SPACE_PIGEON,
                                tournament = NORCAL_VALIDATED_1,
                                result = MatchResult.LOSE
                        ),
                        TournamentMatch(
                                opponent = DIMSUM,
                                tournament = NORCAL_VALIDATED_1,
                                result = MatchResult.LOSE
                        ),
                        TournamentMatch(
                                opponent = NMW,
                                tournament = MELEE_AT_THE_MADE_125,
                                result = MatchResult.LOSE
                        ),
                        TournamentMatch(
                                opponent = GRANDMA_DAN,
                                tournament = MELEE_AT_THE_MADE_125,
                                result = MatchResult.WIN
                        ),
                        TournamentMatch(
                                opponent = SNAP,
                                tournament = MELEE_AT_THE_MADE_125,
                                result = MatchResult.LOSE
                        )
                )
        )

        private val EMPTY_MATCHES_BUNDLE = MatchesBundle(
                player = RAGTIME_MOUSE,
                losses = 0,
                wins = 0
        )

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )
    }

    private class ServerApiOverride(
            internal var matchesBundle: MatchesBundle? = null
    ) : AbsServerApi() {

        override fun getMatches(region: Region, playerId: String): Single<MatchesBundle> {
            val matchesBundle = this.matchesBundle

            return if (matchesBundle == null) {
                Single.error(NullPointerException())
            } else {
                Single.just(matchesBundle)
            }
        }

    }

}
