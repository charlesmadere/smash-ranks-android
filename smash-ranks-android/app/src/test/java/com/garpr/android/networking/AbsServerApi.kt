package com.garpr.android.networking

import com.garpr.android.models.*

abstract class AbsServerApi : ServerApi {

    override fun getHeadToHead(region: Region, playerId: String, opponentId: String,
            listener: ApiListener<HeadToHead>) {
        throw RuntimeException()
    }

    override fun getMatches(region: Region, playerId: String,
            listener: ApiListener<MatchesBundle>) {
        throw RuntimeException()
    }

    override fun getPlayer(region: Region, playerId: String, listener: ApiListener<FullPlayer>) {
        throw RuntimeException()
    }

    override fun getPlayerMatches(region: Region, playerId: String,
            listener: ApiListener<PlayerMatchesBundle>) {
        throw RuntimeException()
    }

    override fun getPlayers(region: Region, listener: ApiListener<PlayersBundle>) {
        throw RuntimeException()
    }

    override fun getRankings(region: Region, listener: ApiListener<RankingsBundle>) {
        throw RuntimeException()
    }

    override fun getRegions(endpoint: Endpoint?, listener: ApiListener<RegionsBundle>) {
        throw RuntimeException()
    }

    override fun getSmashRoster(endpoint: Endpoint): ServerResponse<Map<String, SmashCompetitor>> {
        throw RuntimeException()
    }

    override fun getTournament(region: Region, tournamentId: String,
            listener: ApiListener<FullTournament>) {
        throw RuntimeException()
    }

    override fun getTournaments(region: Region, listener: ApiListener<TournamentsBundle>) {
        throw RuntimeException()
    }

}
