package com.garpr.android.networking

import com.garpr.android.models.*

interface ServerApi {

    fun getHeadToHead(region: Region, playerId: String, opponentId: String,
            listener: ApiListener<HeadToHead>)

    fun getMatches(region: Region, playerId: String, listener: ApiListener<MatchesBundle>)

    fun getPlayer(region: Region, playerId: String, listener: ApiListener<FullPlayer>)

    fun getPlayerMatches(region: Region, playerId: String,
            listener: ApiListener<PlayerMatchesBundle>)

    fun getPlayers(region: Region, listener: ApiListener<PlayersBundle>)

    fun getRankings(region: Region, listener: ApiListener<RankingsBundle>)

    fun getRegions(listener: ApiListener<RegionsBundle>)

    fun getTournament(region: Region, tournamentId: String, listener: ApiListener<FullTournament>)

    fun getTournaments(region: Region, listener: ApiListener<TournamentsBundle>)

}
