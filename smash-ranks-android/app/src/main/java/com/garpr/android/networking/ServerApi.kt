package com.garpr.android.networking

import androidx.annotation.AnyThread
import androidx.annotation.WorkerThread
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.HeadToHead
import com.garpr.android.data.models.MatchesBundle
import com.garpr.android.data.models.PlayerMatchesBundle
import com.garpr.android.data.models.PlayersBundle
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.RegionsBundle
import com.garpr.android.data.models.ServerResponse
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.data.models.TournamentsBundle

interface ServerApi {

    fun getHeadToHead(region: Region, playerId: String, opponentId: String,
            listener: ApiListener<HeadToHead>)

    fun getMatches(region: Region, playerId: String, listener: ApiListener<MatchesBundle>)

    fun getPlayer(region: Region, playerId: String, listener: ApiListener<FullPlayer>)

    fun getPlayerMatches(region: Region, playerId: String, listener: ApiListener<PlayerMatchesBundle>)

    fun getPlayers(region: Region, listener: ApiListener<PlayersBundle>)

    @AnyThread
    fun getRankings(region: Region, listener: ApiListener<RankingsBundle>)

    fun getRegions(endpoint: Endpoint? = null, listener: ApiListener<RegionsBundle>)

    @WorkerThread
    fun getSmashRoster(endpoint: Endpoint): ServerResponse<Map<String, SmashCompetitor>>

    fun getTournament(region: Region, tournamentId: String, listener: ApiListener<FullTournament>)

    fun getTournaments(region: Region, listener: ApiListener<TournamentsBundle>)

}
