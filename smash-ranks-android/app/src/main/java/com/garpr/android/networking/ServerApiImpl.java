package com.garpr.android.networking;

import android.support.annotation.NonNull;

import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.Timber;
import com.garpr.android.models.FullPlayer;
import com.garpr.android.models.FullTournament;
import com.garpr.android.models.HeadToHead;
import com.garpr.android.models.MatchesBundle;
import com.garpr.android.models.PlayersBundle;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.models.RegionsBundle;
import com.garpr.android.models.TournamentsBundle;
import com.garpr.android.preferences.RankingsPollingPreferenceStore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerApiImpl implements ServerApi {

    private static final String TAG = "ServerApiImpl";

    private final GarPrApi mGarPrApi;
    private final RankingsPollingPreferenceStore mRankingsPollingPreferenceStore;
    private final RegionManager mRegionManager;
    private final Timber mTimber;


    public ServerApiImpl(@NonNull final GarPrApi garPrApi,
            @NonNull final RankingsPollingPreferenceStore rankingsPollingPreferenceStore,
            @NonNull final RegionManager regionManager, @NonNull final Timber timber) {
        mGarPrApi = garPrApi;
        mRankingsPollingPreferenceStore = rankingsPollingPreferenceStore;
        mRegionManager = regionManager;
        mTimber = timber;
    }

    @Override
    public void getHeadToHead(@NonNull final String region, @NonNull final String playerId,
            @NonNull final String opponentId, @NonNull final ApiListener<HeadToHead> listener) {
        mGarPrApi.getHeadToHead(region, playerId, opponentId).enqueue(new Callback<HeadToHead>() {
            @Override
            public void onResponse(final Call<HeadToHead> call,
                    final Response<HeadToHead> response) {
                if (response.isSuccessful()) {
                    listener.success(response.body());
                } else {
                    mTimber.e(TAG, "getHeadToHead (" + region + ") (" + playerId + ") (" +
                            opponentId + ") failed (code " + response.code() + ")");
                    listener.failure();
                }
            }

            @Override
            public void onFailure(final Call<HeadToHead> call, final Throwable t) {
                mTimber.e(TAG, "getHeadToHead (" + region + ") (" + playerId + ") (" +
                        opponentId + ") failed", t);
                listener.failure();
            }
        });
    }

    @Override
    public void getMatches(@NonNull final String region, @NonNull final String playerId,
            @NonNull final ApiListener<MatchesBundle> listener) {
        mGarPrApi.getMatches(region, playerId).enqueue(new Callback<MatchesBundle>() {
            @Override
            public void onResponse(final Call<MatchesBundle> call,
                    final Response<MatchesBundle> response) {
                if (response.isSuccessful()) {
                    listener.success(response.body());
                } else {
                    mTimber.e(TAG, "getMatches (" + region + ") (" + playerId + ") failed (code "
                            + response.code() + ")");
                    listener.failure();
                }
            }

            @Override
            public void onFailure(final Call<MatchesBundle> call, final Throwable t) {
                mTimber.e(TAG, "getMatches (" + region + ") (" + playerId + ") failed", t);
                listener.failure();
            }
        });
    }

    @Override
    public void getPlayer(@NonNull final String region, @NonNull final String playerId,
            @NonNull final ApiListener<FullPlayer> listener) {
        mGarPrApi.getPlayer(region, playerId).enqueue(new Callback<FullPlayer>() {
            @Override
            public void onResponse(final Call<FullPlayer> call, final Response<FullPlayer> response) {
                if (response.isSuccessful()) {
                    listener.success(response.body());
                } else {
                    mTimber.e(TAG, "getPlayer (" + region + ") (" + playerId + ") failed (code "
                            + response.code() + ")");
                    listener.failure();
                }
            }

            @Override
            public void onFailure(final Call<FullPlayer> call, final Throwable t) {
                mTimber.e(TAG, "getPlayer (" + region + ") (" + playerId + ") failed", t);
                listener.failure();
            }
        });
    }

    @Override
    public void getPlayers(@NonNull final String region,
            @NonNull final ApiListener<PlayersBundle> listener) {
        mGarPrApi.getPlayers(region).enqueue(new Callback<PlayersBundle>() {
            @Override
            public void onResponse(final Call<PlayersBundle> call,
                    final Response<PlayersBundle> response) {
                if (response.isSuccessful()) {
                    listener.success(response.body());
                } else {
                    mTimber.e(TAG, "getPlayers (" + region + ") failed (code " + response.code()
                            + ")");
                    listener.failure();
                }
            }

            @Override
            public void onFailure(final Call<PlayersBundle> call, final Throwable t) {
                mTimber.e(TAG, "getPlayers (" + region + ") failed", t);
                listener.failure();
            }
        });
    }

    @Override
    public void getRankings(@NonNull final String region,
            @NonNull final ApiListener<RankingsBundle> listener) {
        mGarPrApi.getRankings(region).enqueue(new Callback<RankingsBundle>() {
            @Override
            public void onResponse(final Call<RankingsBundle> call,
                    final Response<RankingsBundle> response) {
                final RankingsBundle body = response.isSuccessful() ? response.body() : null;

                if (body != null && region.equals(mRegionManager.getRegion())) {
                    mRankingsPollingPreferenceStore.getRankingsDate().set(body.getTime());
                }

                if (response.isSuccessful()) {
                    listener.success(body);
                } else {
                    mTimber.e(TAG, "getRankings (" + region + ") failed (code " + response.code()
                            + ")");
                    listener.failure();
                }
            }

            @Override
            public void onFailure(final Call<RankingsBundle> call, final Throwable t) {
                mTimber.e(TAG, "getRankings (" + region + ") failed", t);
                listener.failure();
            }
        });
    }

    @Override
    public void getRegions(@NonNull final ApiListener<RegionsBundle> listener) {
        mGarPrApi.getRegions().enqueue(new Callback<RegionsBundle>() {
            @Override
            public void onResponse(final Call<RegionsBundle> call,
                    final Response<RegionsBundle> response) {
                if (response.isSuccessful()) {
                    listener.success(response.body());
                } else {
                    mTimber.e(TAG, "getRegions failed (code " + response.code() + ")");
                    listener.failure();
                }
            }

            @Override
            public void onFailure(final Call<RegionsBundle> call, final Throwable t) {
                mTimber.e(TAG, "getRegions failed", t);
                listener.failure();
            }
        });
    }

    @Override
    public void getTournament(@NonNull final String region, @NonNull final String tournamentId,
            @NonNull final ApiListener<FullTournament> listener) {
        mGarPrApi.getTournament(region, tournamentId).enqueue(new Callback<FullTournament>() {
            @Override
            public void onResponse(final Call<FullTournament> call,
                    final Response<FullTournament> response) {
                if (response.isSuccessful()) {
                    listener.success(response.body());
                } else {
                    mTimber.e(TAG, "getTournament (" + region + ") (" + tournamentId +
                            ") failed (code " + response.code() + ")");
                    listener.failure();
                }
            }

            @Override
            public void onFailure(final Call<FullTournament> call, final Throwable t) {
                mTimber.e(TAG, "getTournament (" + region + ") (" + tournamentId + ") failed", t);
                listener.failure();
            }
        });
    }

    @Override
    public void getTournaments(@NonNull final String region,
            @NonNull final ApiListener<TournamentsBundle> listener) {
        mGarPrApi.getTournaments(region).enqueue(new Callback<TournamentsBundle>() {
            @Override
            public void onResponse(final Call<TournamentsBundle> call,
                    final Response<TournamentsBundle> response) {
                if (response.isSuccessful()) {
                    listener.success(response.body());
                } else {
                    mTimber.e(TAG, "getTournaments (" + region + ") failed (code " +
                            response.code() + ")");
                    listener.failure();
                }
            }

            @Override
            public void onFailure(final Call<TournamentsBundle> call, final Throwable t) {
                mTimber.e(TAG, "getTournaments (" + region + ") failed", t);
                listener.failure();
            }
        });
    }

}
