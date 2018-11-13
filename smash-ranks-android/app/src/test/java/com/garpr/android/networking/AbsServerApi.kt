package com.garpr.android.networking

import com.garpr.android.models.Endpoint
import com.garpr.android.models.FullPlayer
import com.garpr.android.models.FullTournament
import com.garpr.android.models.HeadToHead
import com.garpr.android.models.MatchesBundle
import com.garpr.android.models.PlayerMatchesBundle
import com.garpr.android.models.PlayersBundle
import com.garpr.android.models.RankingsBundle
import com.garpr.android.models.Region
import com.garpr.android.models.RegionsBundle
import com.garpr.android.models.ServerResponse
import com.garpr.android.models.SmashCompetitor
import com.garpr.android.models.TournamentsBundle

abstract class AbsServerApi : ServerApi {

    override fun getHeadToHead(region: Region, playerId: String, opponentId: String,
            listener: ApiListener<HeadToHead>) {
        throw NotImplementedError()
    }

    override fun getMatches(region: Region, playerId: String,
            listener: ApiListener<MatchesBundle>) {
        throw NotImplementedError()
    }

    override fun getPlayer(region: Region, playerId: String, listener: ApiListener<FullPlayer>) {
        throw NotImplementedError()
    }

    override fun getPlayerMatches(region: Region, playerId: String,
            listener: ApiListener<PlayerMatchesBundle>) {
        throw NotImplementedError()
    }

    override fun getPlayers(region: Region, listener: ApiListener<PlayersBundle>) {
        throw NotImplementedError()
    }

    override fun getRankings(region: Region, listener: ApiListener<RankingsBundle>) {
        throw NotImplementedError()
    }

    override fun getRegions(endpoint: Endpoint?, listener: ApiListener<RegionsBundle>) {
        throw NotImplementedError()
    }

    override fun getSmashRoster(endpoint: Endpoint): ServerResponse<Map<String, SmashCompetitor>> {
        throw NotImplementedError()
    }

    override fun getTournament(region: Region, tournamentId: String,
            listener: ApiListener<FullTournament>) {
        throw NotImplementedError()
    }

    override fun getTournaments(region: Region, listener: ApiListener<TournamentsBundle>) {
        throw NotImplementedError()
    }

}
