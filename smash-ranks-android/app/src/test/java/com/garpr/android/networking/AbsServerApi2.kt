package com.garpr.android.networking

import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.HeadToHead
import com.garpr.android.data.models.MatchesBundle
import com.garpr.android.data.models.PlayersBundle
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.RegionsBundle
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.data.models.TournamentsBundle
import io.reactivex.Single

abstract class AbsServerApi2 : ServerApi2 {

    override fun getHeadToHead(region: Region, playerId: String, opponentId: String): Single<HeadToHead> {
        throw NotImplementedError()
    }

    override fun getMatches(region: Region, playerId: String): Single<MatchesBundle> {
        throw NotImplementedError()
    }

    override fun getPlayer(region: Region, playerId: String): Single<FullPlayer> {
        throw NotImplementedError()
    }

    override fun getPlayers(region: Region): Single<PlayersBundle> {
        throw NotImplementedError()
    }

    override fun getRankings(region: Region): Single<RankingsBundle> {
        throw NotImplementedError()
    }

    override fun getRegions(endpoint: Endpoint): Single<RegionsBundle> {
        throw NotImplementedError()
    }

    override fun getSmashRoster(endpoint: Endpoint): Single<Map<String, SmashCompetitor>> {
        throw NotImplementedError()
    }

    override fun getTournament(region: Region, tournamentId: String): Single<FullTournament> {
        throw NotImplementedError()
    }

    override fun getTournaments(region: Region): Single<TournamentsBundle> {
        throw NotImplementedError()
    }

}
