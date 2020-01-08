package com.garpr.android.repositories

import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.MatchesBundle
import com.garpr.android.data.models.PlayerMatchesBundle
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.TournamentMatch
import com.garpr.android.misc.Schedulers
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.Collections

class PlayerMatchesRepositoryImpl(
        private val matchesRepository: MatchesRepository,
        private val playersRepository: PlayersRepository,
        private val schedulers: Schedulers
) : PlayerMatchesRepository {

    override fun getPlayerAndMatches(region: Region, playerId: String): Single<PlayerMatchesBundle> {
        return Single.zip(
                matchesRepository.getMatches(region, playerId),
                playersRepository.getPlayer(region, playerId),
                BiFunction<MatchesBundle, FullPlayer, PlayerMatchesBundle> { t1, t2 ->
                    mergeResponses(fullPlayer = t2, matchesBundle = t1)
                })
                .subscribeOn(schedulers.background)
    }

    private fun mergeResponses(fullPlayer: FullPlayer, matchesBundle: MatchesBundle): PlayerMatchesBundle {
        if (!matchesBundle.matches.isNullOrEmpty()) {
            Collections.sort(matchesBundle.matches, TournamentMatch.REVERSE_CHRONOLOGICAL_ORDER)
        }

        return PlayerMatchesBundle(fullPlayer = fullPlayer, matchesBundle = matchesBundle)
    }

}
