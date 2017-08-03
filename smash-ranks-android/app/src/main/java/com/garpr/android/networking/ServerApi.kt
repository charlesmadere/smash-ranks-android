package com.garpr.android.networking

import com.garpr.android.models.*

interface ServerApi {

    fun getHeadToHead(region: LiteRegion, playerId: String, opponentId: String,
            listener: ApiListener<HeadToHead>)

    fun getMatches(region: LiteRegion, playerId: String, listener: ApiListener<MatchesBundle>)

    fun getPlayer(region: LiteRegion, playerId: String, listener: ApiListener<FullPlayer>)

    fun getPlayerMatches(region: LiteRegion, playerId: String,
            listener: ApiListener<PlayerMatchesBundle>)

    fun getPlayers(region: LiteRegion, listener: ApiListener<PlayersBundle>)

    fun getRankings(region: AbsRegion, listener: ApiListener<RankingsBundle>)

    fun getRegions(listener: ApiListener<RegionsBundle>)

    fun getTournament(region: AbsRegion, tournamentId: String, listener: ApiListener<FullTournament>)

    fun getTournaments(region: AbsRegion, listener: ApiListener<TournamentsBundle>)

}
