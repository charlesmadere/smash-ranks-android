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
import com.garpr.android.extensions.require
import io.reactivex.Single

class ServerApiImpl(
        garPrApi: GarPrApi,
        notGarPrApi: GarPrApi,
        private val smashRosterApi: SmashRosterApi
) : ServerApi {

    private val apis = mapOf(
            Endpoint.GAR_PR to garPrApi,
            Endpoint.NOT_GAR_PR to notGarPrApi
    )

    override fun getHeadToHead(region: Region, playerId: String,
            opponentId: String): Single<HeadToHead> {
        return apis.require(region.endpoint).getHeadToHead(region.id, playerId, opponentId)
    }

    override fun getMatches(region: Region, playerId: String): Single<MatchesBundle> {
        return apis.require(region.endpoint).getMatches(region.id, playerId)
    }

    override fun getPlayer(region: Region, playerId: String): Single<FullPlayer> {
        return apis.require(region.endpoint).getPlayer(region.id, playerId)
    }

    override fun getPlayers(region: Region): Single<PlayersBundle> {
        return apis.require(region.endpoint).getPlayers(region.id)
    }

    override fun getRankings(region: Region): Single<RankingsBundle> {
        return apis.require(region.endpoint).getRankings(region.id)
    }

    override fun getRegions(endpoint: Endpoint): Single<RegionsBundle> {
        return apis.require(endpoint).getRegions()
    }

    override fun getSmashRoster(endpoint: Endpoint): Single<Map<String, SmashCompetitor>> {
        return when (endpoint) {
            Endpoint.GAR_PR -> smashRosterApi.getGarPrJson()
            Endpoint.NOT_GAR_PR -> smashRosterApi.getNotGarPrJson()
        }
    }

    override fun getTournament(region: Region, tournamentId: String): Single<FullTournament> {
        return apis.require(region.endpoint).getTournament(region.id, tournamentId)
    }

    override fun getTournaments(region: Region): Single<TournamentsBundle> {
        return apis.require(region.endpoint).getTournaments(region.id)
    }

}
