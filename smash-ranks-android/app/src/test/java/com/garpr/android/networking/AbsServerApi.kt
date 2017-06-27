package com.garpr.android.networking

import com.garpr.android.models.FullPlayer
import com.garpr.android.models.FullTournament
import com.garpr.android.models.HeadToHead
import com.garpr.android.models.MatchesBundle
import com.garpr.android.models.PlayerMatchesBundle
import com.garpr.android.models.PlayersBundle
import com.garpr.android.models.RankingsBundle
import com.garpr.android.models.Region
import com.garpr.android.models.RegionsBundle
import com.garpr.android.models.TournamentsBundle

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

    override fun getRegions(listener: ApiListener<RegionsBundle>) {
        throw RuntimeException()
    }

    override fun getTournament(region: Region, tournamentId: String,
            listener: ApiListener<FullTournament>?) {
        throw RuntimeException()
    }

    override fun getTournaments(region: Region, listener: ApiListener<TournamentsBundle>) {
        throw RuntimeException()
    }

}
