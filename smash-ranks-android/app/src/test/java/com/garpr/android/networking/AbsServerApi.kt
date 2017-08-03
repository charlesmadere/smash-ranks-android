package com.garpr.android.networking

import com.garpr.android.models.*

abstract class AbsServerApi : ServerApi {

    override fun getHeadToHead(region: LiteRegion, playerId: String, opponentId: String,
            listener: ApiListener<HeadToHead>) {
        throw RuntimeException()
    }

    override fun getMatches(region: LiteRegion, playerId: String,
            listener: ApiListener<MatchesBundle>) {
        throw RuntimeException()
    }

    override fun getPlayer(region: LiteRegion, playerId: String, listener: ApiListener<FullPlayer>) {
        throw RuntimeException()
    }

    override fun getPlayerMatches(region: LiteRegion, playerId: String,
            listener: ApiListener<PlayerMatchesBundle>) {
        throw RuntimeException()
    }

    override fun getPlayers(region: LiteRegion, listener: ApiListener<PlayersBundle>) {
        throw RuntimeException()
    }

    override fun getRankings(region: LiteRegion, listener: ApiListener<RankingsBundle>) {
        throw RuntimeException()
    }

    override fun getRegions(listener: ApiListener<RegionsBundle>) {
        throw RuntimeException()
    }

    override fun getTournament(region: LiteRegion, tournamentId: String,
            listener: ApiListener<FullTournament>) {
        throw RuntimeException()
    }

    override fun getTournaments(region: LiteRegion, listener: ApiListener<TournamentsBundle>) {
        throw RuntimeException()
    }

}
