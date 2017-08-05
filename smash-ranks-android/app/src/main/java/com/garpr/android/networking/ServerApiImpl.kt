package com.garpr.android.networking

import com.garpr.android.misc.Constants
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.Timber
import com.garpr.android.models.*
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServerApiImpl(
        private val mGarPrApi: GarPrApi,
        private val mRankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val mRegionManager: RegionManager,
        private val mTimber: Timber
) : ServerApi {

    companion object {
        private const val TAG = "ServerApiImpl"
    }

    override fun getHeadToHead(region: Region, playerId: String, opponentId: String,
            listener: ApiListener<HeadToHead>) {
        val url = region.endpoint.getHeadToHeadApiPath(region.id, playerId, opponentId)

        mGarPrApi.getHeadToHead(url).enqueue(object : Callback<HeadToHead> {
            override fun onResponse(call: Call<HeadToHead>, response: Response<HeadToHead>) {
                if (response.isSuccessful) {
                    listener.success(response.body())
                } else {
                    mTimber.e(TAG, "getHeadToHead ($region) ($playerId) ($opponentId) " +
                            "failed (code " + response.code() + ")")
                    listener.failure(response.code())
                }
            }

            override fun onFailure(call: Call<HeadToHead>, t: Throwable) {
                mTimber.e(TAG, "getHeadToHead ($region) ($playerId) ($opponentId) failed", t)
                listener.failure(Constants.ERROR_CODE_UNKNOWN)
            }
        })
    }

    override fun getMatches(region: Region, playerId: String, listener: ApiListener<MatchesBundle>) {
        val url = region.endpoint.getMatchesApiPath(region.id, playerId)

        mGarPrApi.getMatches(url).enqueue(object : Callback<MatchesBundle> {
            override fun onResponse(call: Call<MatchesBundle>, response: Response<MatchesBundle>) {
                if (response.isSuccessful) {
                    listener.success(response.body())
                } else {
                    mTimber.e(TAG, "getMatches ($region) ($playerId) failed (code " +
                            response.code() + ")")
                    listener.failure(response.code())
                }
            }

            override fun onFailure(call: Call<MatchesBundle>, t: Throwable) {
                mTimber.e(TAG, "getMatches ($region) ($playerId) failed", t)
                listener.failure(Constants.ERROR_CODE_UNKNOWN)
            }
        })
    }

    override fun getPlayer(region: Region, playerId: String, listener: ApiListener<FullPlayer>) {
        val url = region.endpoint.getPlayerApiPath(region.id, playerId)

        mGarPrApi.getPlayer(url).enqueue(object : Callback<FullPlayer> {
            override fun onResponse(call: Call<FullPlayer>, response: Response<FullPlayer>) {
                if (response.isSuccessful) {
                    listener.success(response.body())
                } else {
                    mTimber.e(TAG, "getPlayer ($region) ($playerId) failed (code " +
                            response.code() + ")")
                    listener.failure(response.code())
                }
            }

            override fun onFailure(call: Call<FullPlayer>, t: Throwable) {
                mTimber.e(TAG, "getPlayer ($region) ($playerId) failed", t)
                listener.failure(Constants.ERROR_CODE_UNKNOWN)
            }
        })
    }

    override fun getPlayerMatches(region: Region, playerId: String,
            listener: ApiListener<PlayerMatchesBundle>) {
        PlayerMatchesBundleApiCall(listener, region, this, playerId).fetch()
    }

    override fun getPlayers(region: Region, listener: ApiListener<PlayersBundle>) {
        val url = region.endpoint.getPlayersApiPath(region.id)

        mGarPrApi.getPlayers(url).enqueue(object : Callback<PlayersBundle> {
            override fun onResponse(call: Call<PlayersBundle>, response: Response<PlayersBundle>) {
                if (response.isSuccessful) {
                    listener.success(response.body())
                } else {
                    mTimber.e(TAG, "getPlayers ($region) failed (code " + response.code() + ")")
                    listener.failure(response.code())
                }
            }

            override fun onFailure(call: Call<PlayersBundle>, t: Throwable) {
                mTimber.e(TAG, "getPlayers ($region) failed", t)
                listener.failure(Constants.ERROR_CODE_UNKNOWN)
            }
        })
    }

    override fun getRankings(region: Region, listener: ApiListener<RankingsBundle>) {
        val url = region.endpoint.getRankingsApiPath(region.id)

        mGarPrApi.getRankings(url).enqueue(object : Callback<RankingsBundle> {
            override fun onResponse(call: Call<RankingsBundle>, response: Response<RankingsBundle>) {
                val body = if (response.isSuccessful) response.body() else null

                if (body == null) {
                    mTimber.e(TAG, "getRankings ($region) failed (code " + response.code() + ")")
                    listener.failure(response.code())
                } else {
                    if (region == mRegionManager.getRegion()) {
                        mRankingsPollingPreferenceStore.rankingsDate.set(body.time)
                    }

                    listener.success(body)
                }
            }

            override fun onFailure(call: Call<RankingsBundle>, t: Throwable) {
                mTimber.e(TAG, "getRankings ($region) failed", t)
                listener.failure(Constants.ERROR_CODE_UNKNOWN)
            }
        })
    }

    override fun getRegions(endpoint: Endpoint?, listener: ApiListener<RegionsBundle>) {
        if (endpoint == null) {
            RegionsBundleApiCall(listener, this).fetch()
        } else {
            mGarPrApi.getRegions(endpoint.regionsApiPath).enqueue(object : Callback<RegionsBundle> {
                override fun onResponse(call: Call<RegionsBundle>, response: Response<RegionsBundle>) {
                    val body = if (response.isSuccessful) response.body() else null

                    if (body == null) {
                        mTimber.e(TAG, "getRegions($endpoint) failed (code " + response.code() + ")")
                        listener.failure(response.code())
                    } else {
                        listener.success(body)
                    }
                }

                override fun onFailure(call: Call<RegionsBundle>?, t: Throwable?) {
                    mTimber.e(TAG, "getRegions($endpoint) failed", t)
                    listener.failure(Constants.ERROR_CODE_UNKNOWN)
                }
            })
        }
    }

    override fun getTournament(region: Region, tournamentId: String,
            listener: ApiListener<FullTournament>) {
        val url = region.endpoint.getTournamentApiPath(region.id, tournamentId)

        mGarPrApi.getTournament(url).enqueue(object : Callback<FullTournament> {
            override fun onResponse(call: Call<FullTournament>, response: Response<FullTournament>) {
                if (response.isSuccessful) {
                    listener.success(response.body())
                } else {
                    mTimber.e(TAG, "getTournament ($region) ($tournamentId) failed (code " +
                            response.code() + ")")
                    listener.failure(response.code())
                }
            }

            override fun onFailure(call: Call<FullTournament>, t: Throwable) {
                mTimber.e(TAG, "getTournament ($region) ($tournamentId) failed", t)
                listener.failure(Constants.ERROR_CODE_UNKNOWN)
            }
        })
    }

    override fun getTournaments(region: Region, listener: ApiListener<TournamentsBundle>) {
        val url = region.endpoint.getTournamentsApiPath(region.id)

        mGarPrApi.getTournaments(url).enqueue(object : Callback<TournamentsBundle> {
            override fun onResponse(call: Call<TournamentsBundle>,
                    response: Response<TournamentsBundle>) {
                if (response.isSuccessful) {
                    listener.success(response.body())
                } else {
                    mTimber.e(TAG, "getTournaments ($region) failed (code " +
                            response.code() + ")")
                    listener.failure(response.code())
                }
            }

            override fun onFailure(call: Call<TournamentsBundle>, t: Throwable) {
                mTimber.e(TAG, "getTournaments ($region) failed", t)
                listener.failure(Constants.ERROR_CODE_UNKNOWN)
            }
        })
    }

}
