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

interface ServerApi {

    fun getHeadToHead(region: Region, playerId: String, opponentId: String): Single<HeadToHead>

    fun getMatches(region: Region, playerId: String): Single<MatchesBundle>

    fun getPlayer(region: Region, playerId: String): Single<FullPlayer>

    fun getPlayers(region: Region): Single<PlayersBundle>

    fun getRankings(region: Region): Single<RankingsBundle>

    fun getRegions(endpoint: Endpoint): Single<RegionsBundle>

    fun getSmashRoster(endpoint: Endpoint): Single<Map<String, SmashCompetitor>>

    fun getTournament(region: Region, tournamentId: String): Single<FullTournament>

    fun getTournaments(region: Region): Single<TournamentsBundle>

}
