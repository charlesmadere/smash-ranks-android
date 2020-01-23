package com.garpr.android.repositories

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.TournamentsBundle
import com.garpr.android.networking.ServerApi
import io.reactivex.Single
import java.util.Collections

class TournamentsRepositoryImpl(
        private val serverApi: ServerApi
) : TournamentsRepository {

    override fun getTournament(region: Region, tournamentId: String): Single<FullTournament> {
        return serverApi.getTournament(region, tournamentId)
                .doOnSuccess {
                    if (!it.players.isNullOrEmpty()) {
                        Collections.sort(it.players, AbsPlayer.ALPHABETICAL_ORDER)
                    }
                }
    }

    override fun getTournaments(region: Region): Single<TournamentsBundle> {
        return serverApi.getTournaments(region)
                .doOnSuccess {
                    if (!it.tournaments.isNullOrEmpty()) {
                        Collections.sort(it.tournaments, AbsTournament.REVERSE_CHRONOLOGICAL_ORDER)
                    }
                }
    }

}
