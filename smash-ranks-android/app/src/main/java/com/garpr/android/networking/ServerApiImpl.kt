package com.garpr.android.networking

import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.FullTournamentUtils
import com.garpr.android.misc.Timber
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
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServerApiImpl(
        private val fullTournamentUtils: FullTournamentUtils,
        private val garPrApi: GarPrApi,
        private val notGarPrApi: GarPrApi,
        private val rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val regionManager: RegionManager,
        private val smashRosterApi: SmashRosterApi,
        private val timber: Timber
) : ServerApi {

    companion object {
        private const val TAG = "ServerApiImpl"
    }

    private fun getGarPrApi(endpoint: Endpoint): GarPrApi {
        return if (endpoint == Endpoint.GAR_PR) garPrApi else notGarPrApi
    }

    private fun getGarPrApi(region: Region): GarPrApi {
        return getGarPrApi(region.endpoint)
    }

    override fun getHeadToHead(region: Region, playerId: String, opponentId: String,
            listener: ApiListener<HeadToHead>) {
        getGarPrApi(region).getHeadToHead(region.id, playerId, opponentId).enqueue(object : Callback<HeadToHead> {
            override fun onResponse(call: Call<HeadToHead>, response: Response<HeadToHead>) {
                if (response.isSuccessful) {
                    listener.success(response.body())
                } else {
                    timber.e(TAG, "getHeadToHead ($region) ($playerId) ($opponentId) " +
                            "failed (code ${response.code()})")
                    listener.failure(response.code())
                }
            }

            override fun onFailure(call: Call<HeadToHead>, t: Throwable) {
                timber.e(TAG, "getHeadToHead ($region) ($playerId) ($opponentId) failed", t)
                listener.failure()
            }
        })
    }

    override fun getMatches(region: Region, playerId: String, listener: ApiListener<MatchesBundle>) {
        getGarPrApi(region).getMatches(region.id, playerId).enqueue(object : Callback<MatchesBundle> {
            override fun onResponse(call: Call<MatchesBundle>, response: Response<MatchesBundle>) {
                if (response.isSuccessful) {
                    listener.success(response.body())
                } else {
                    timber.e(TAG, "getMatches ($region) ($playerId) failed " +
                            "(code ${response.code()})")
                    listener.failure(response.code())
                }
            }

            override fun onFailure(call: Call<MatchesBundle>, t: Throwable) {
                timber.e(TAG, "getMatches ($region) ($playerId) failed", t)
                listener.failure()
            }
        })
    }

    override fun getPlayer(region: Region, playerId: String, listener: ApiListener<FullPlayer>) {
        getGarPrApi(region).getPlayer(region.id, playerId).enqueue(object : Callback<FullPlayer> {
            override fun onResponse(call: Call<FullPlayer>, response: Response<FullPlayer>) {
                if (response.isSuccessful) {
                    listener.success(response.body())
                } else {
                    timber.e(TAG, "getPlayer ($region) ($playerId) failed " +
                            "(code ${response.code()})")
                    listener.failure(response.code())
                }
            }

            override fun onFailure(call: Call<FullPlayer>, t: Throwable) {
                timber.e(TAG, "getPlayer ($region) ($playerId) failed", t)
                listener.failure()
            }
        })
    }

    override fun getPlayerMatches(region: Region, playerId: String,
            listener: ApiListener<PlayerMatchesBundle>) {
        PlayerMatchesBundleApiCall(listener, region, this, playerId).fetch()
    }

    override fun getPlayers(region: Region, listener: ApiListener<PlayersBundle>) {
        getGarPrApi(region).getPlayers(region.id).enqueue(object : Callback<PlayersBundle> {
            override fun onResponse(call: Call<PlayersBundle>, response: Response<PlayersBundle>) {
                if (response.isSuccessful) {
                    listener.success(response.body())
                } else {
                    timber.e(TAG, "getPlayers ($region) failed (code ${response.code()})")
                    listener.failure(response.code())
                }
            }

            override fun onFailure(call: Call<PlayersBundle>, t: Throwable) {
                timber.e(TAG, "getPlayers ($region) failed", t)
                listener.failure()
            }
        })
    }

    override fun getRankings(region: Region, listener: ApiListener<RankingsBundle>) {
        getGarPrApi(region).getRankings(region.id).enqueue(object : Callback<RankingsBundle> {
            override fun onResponse(call: Call<RankingsBundle>, response: Response<RankingsBundle>) {
                val body = if (response.isSuccessful) response.body() else null

                if (body == null) {
                    timber.e(TAG, "getRankings ($region) failed (code ${response.code()})")
                    listener.failure(response.code())
                } else {
                    if (region == regionManager.getRegion()) {
                        rankingsPollingPreferenceStore.rankingsId.set(body.id)
                    }

                    listener.success(body)
                }
            }

            override fun onFailure(call: Call<RankingsBundle>, t: Throwable) {
                timber.e(TAG, "getRankings ($region) failed", t)
                listener.failure()
            }
        })
    }

    override fun getRegions(endpoint: Endpoint?, listener: ApiListener<RegionsBundle>) {
        if (endpoint == null) {
            RegionsBundleApiCall(listener, this).fetch()
        } else {
            getGarPrApi(endpoint).getRegions().enqueue(object : Callback<RegionsBundle> {
                override fun onResponse(call: Call<RegionsBundle>, response: Response<RegionsBundle>) {
                    val body = if (response.isSuccessful) response.body() else null

                    if (body == null) {
                        timber.e(TAG, "getRegions ($endpoint) failed (code ${response.code()})")
                        listener.failure(response.code())
                    } else {
                        listener.success(body)
                    }
                }

                override fun onFailure(call: Call<RegionsBundle>?, t: Throwable?) {
                    timber.e(TAG, "getRegions ($endpoint) failed", t)
                    listener.failure()
                }
            })
        }
    }

    override fun getSmashRoster(endpoint: Endpoint): ServerResponse<Map<String, SmashCompetitor>> {
        val response = when (endpoint) {
            Endpoint.GAR_PR -> {
                smashRosterApi.getGarPrJson().execute()
            }

            Endpoint.NOT_GAR_PR -> {
                smashRosterApi.getNotGarPrJson().execute()
            }
        }

        return ServerResponse(if (response.isSuccessful) response.body() else null,
                response.isSuccessful, response.code(), response.message())
    }

    override fun getTournament(region: Region, tournamentId: String,
            listener: ApiListener<FullTournament>) {
        getGarPrApi(region).getTournament(region.id, tournamentId).enqueue(object : Callback<FullTournament> {
            override fun onResponse(call: Call<FullTournament>, response: Response<FullTournament>) {
                if (response.isSuccessful) {
                    fullTournamentUtils.prepareFullTournament(response.body(), object : FullTournamentUtils.Callback {
                        override fun onComplete(fullTournament: FullTournament?) {
                            listener.success(fullTournament)
                        }
                    })
                } else {
                    timber.e(TAG, "getTournament ($region) ($tournamentId) failed " +
                            "(code ${response.code()})")
                    listener.failure(response.code())
                }
            }

            override fun onFailure(call: Call<FullTournament>, t: Throwable) {
                timber.e(TAG, "getTournament ($region) ($tournamentId) failed", t)
                listener.failure()
            }
        })
    }

    override fun getTournaments(region: Region, listener: ApiListener<TournamentsBundle>) {
        getGarPrApi(region).getTournaments(region.id).enqueue(object : Callback<TournamentsBundle> {
            override fun onResponse(call: Call<TournamentsBundle>, response: Response<TournamentsBundle>) {
                if (response.isSuccessful) {
                    listener.success(response.body())
                } else {
                    timber.e(TAG, "getTournaments ($region) failed (code ${response.code()})")
                    listener.failure(response.code())
                }
            }

            override fun onFailure(call: Call<TournamentsBundle>, t: Throwable) {
                timber.e(TAG, "getTournaments ($region) failed", t)
                listener.failure()
            }
        })
    }

}
