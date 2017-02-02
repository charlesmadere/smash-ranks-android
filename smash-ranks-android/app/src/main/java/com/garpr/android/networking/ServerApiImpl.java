package com.garpr.android.networking;

import android.support.annotation.NonNull;

import com.garpr.android.misc.Timber;
import com.garpr.android.models.FullPlayer;
import com.garpr.android.models.FullTournament;
import com.garpr.android.models.PlayersBundle;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.models.TournamentsBundle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerApiImpl implements ServerApi {

    private static final String TAG = "ServerApiImpl";

    private final GarPrApi mGarPrApi;
    private final Timber mTimber;


    public ServerApiImpl(final GarPrApi garPrApi, final Timber timber) {
        mGarPrApi = garPrApi;
        mTimber = timber;
    }

    @Override
    public void getPlayer(@NonNull final String region, @NonNull final String playerId,
            @NonNull final ApiListener<FullPlayer> apiCall) {
        mGarPrApi.getPlayer(region, playerId).enqueue(new Callback<FullPlayer>() {
            @Override
            public void onResponse(final Call<FullPlayer> call, final Response<FullPlayer> response) {
                if (response.isSuccessful()) {
                    apiCall.success(response.body());
                } else {
                    mTimber.e(TAG, "getPlayer (" + region + ") (" + playerId + ") failed (code "
                            + response.code() + ")");
                    apiCall.failure();
                }
            }

            @Override
            public void onFailure(final Call<FullPlayer> call, final Throwable t) {
                mTimber.e(TAG, "getPlayer (" + region + ") (" + playerId + ") failed", t);
                apiCall.failure();
            }
        });
    }

    @Override
    public void getPlayers(@NonNull final String region,
            @NonNull final ApiListener<PlayersBundle> apiCall) {
        mGarPrApi.getPlayers(region).enqueue(new Callback<PlayersBundle>() {
            @Override
            public void onResponse(final Call<PlayersBundle> call,
                    final Response<PlayersBundle> response) {
                if (response.isSuccessful()) {
                    apiCall.success(response.body());
                } else {
                    mTimber.e(TAG, "getPlayers (" + region + ") failed (code " + response.code()
                            + ")");
                    apiCall.failure();
                }
            }

            @Override
            public void onFailure(final Call<PlayersBundle> call, final Throwable t) {
                mTimber.e(TAG, "getPlayers (" + region + ") failed", t);
                apiCall.failure();
            }
        });
    }

    @Override
    public void getRankings(@NonNull final String region,
            @NonNull final ApiListener<RankingsBundle> apiCall) {
        mGarPrApi.getRankings(region).enqueue(new Callback<RankingsBundle>() {
            @Override
            public void onResponse(final Call<RankingsBundle> call,
                    final Response<RankingsBundle> response) {
                if (response.isSuccessful()) {
                    apiCall.success(response.body());
                } else {
                    mTimber.e(TAG, "getRankings (" + region + ") failed (code " + response.code()
                            + ")");
                    apiCall.failure();
                }
            }

            @Override
            public void onFailure(final Call<RankingsBundle> call, final Throwable t) {
                mTimber.e(TAG, "getRankings (" + region + ") failed", t);
                apiCall.failure();
            }
        });
    }

    @Override
    public void getTournament(@NonNull final String region, @NonNull final String tournamentId,
            @NonNull final ApiListener<FullTournament> apiCall) {
        mGarPrApi.getTournament(region, tournamentId).enqueue(new Callback<FullTournament>() {
            @Override
            public void onResponse(final Call<FullTournament> call,
                    final Response<FullTournament> response) {
                if (response.isSuccessful()) {
                    apiCall.success(response.body());
                } else {
                    mTimber.e(TAG, "getTournament (" + region + ") (" + tournamentId +
                            ") failed (code " + response.code() + ")");
                    apiCall.failure();
                }
            }

            @Override
            public void onFailure(final Call<FullTournament> call, final Throwable t) {
                mTimber.e(TAG, "getTournament (" + region + ") (" + tournamentId + ") failed", t);
                apiCall.failure();
            }
        });
    }

    @Override
    public void getTournaments(@NonNull final String region,
            @NonNull final ApiListener<TournamentsBundle> apiCall) {
        mGarPrApi.getTournaments(region).enqueue(new Callback<TournamentsBundle>() {
            @Override
            public void onResponse(final Call<TournamentsBundle> call,
                    final Response<TournamentsBundle> response) {
                if (response.isSuccessful()) {
                    apiCall.success(response.body());
                } else {
                    mTimber.e(TAG, "getTournaments (" + region + ") failed (code " +
                            response.code() + ")");
                    apiCall.failure();
                }
            }

            @Override
            public void onFailure(final Call<TournamentsBundle> call, final Throwable t) {
                mTimber.e(TAG, "getTournaments (" + region + ") failed", t);
                apiCall.failure();
            }
        });
    }

}
