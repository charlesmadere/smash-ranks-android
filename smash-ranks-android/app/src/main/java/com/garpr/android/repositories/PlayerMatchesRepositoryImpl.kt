package com.garpr.android.repositories

import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.MatchesBundle
import com.garpr.android.data.models.PlayerMatchesBundle
import com.garpr.android.data.models.Region
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class PlayerMatchesRepositoryImpl(
        private val matchesRepository: MatchesRepository,
        private val playersRepository: PlayersRepository
) : PlayerMatchesRepository {

    override fun getPlayerAndMatches(region: Region, playerId: String): Single<PlayerMatchesBundle> {
        return Single.zip(
                matchesRepository.getMatches(region, playerId),
                playersRepository.getPlayer(region, playerId),
                BiFunction<MatchesBundle, FullPlayer, PlayerMatchesBundle> { t1, t2 ->
                    PlayerMatchesBundle(fullPlayer = t2, matchesBundle = t1)
                })
    }

}
