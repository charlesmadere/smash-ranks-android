package com.garpr.android.networking;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.misc.Constants;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.Timber;
import com.garpr.android.models.Endpoint;
import com.garpr.android.models.FullPlayer;
import com.garpr.android.models.FullTournament;
import com.garpr.android.models.HeadToHead;
import com.garpr.android.models.MatchesBundle;
import com.garpr.android.models.PlayerMatchesBundle;
import com.garpr.android.models.PlayersBundle;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.models.Region;
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
    public void getHeadToHead(@NonNull final Region region, @NonNull final String playerId,
            @NonNull final String opponentId, @NonNull final ApiListener<HeadToHead> listener) {
        String url = region.getEndpoint().getHeadToHeadApiPath(region.getId(), playerId, opponentId);

        mGarPrApi.getHeadToHead(url).enqueue(new Callback<HeadToHead>() {
            @Override
            public void onResponse(final Call<HeadToHead> call,
                    final Response<HeadToHead> response) {
                if (response.isSuccessful()) {
                    listener.success(response.body());
                } else {
                    mTimber.e(TAG, "getHeadToHead (" + region + ") (" + playerId + ") (" +
                            opponentId + ") failed (code " + response.code() + ")");
                    listener.failure(response.code());
                }
            }

            @Override
            public void onFailure(final Call<HeadToHead> call, final Throwable t) {
                mTimber.e(TAG, "getHeadToHead (" + region + ") (" + playerId + ") (" +
                        opponentId + ") failed", t);
                listener.failure(Constants.ERROR_CODE_UNKNOWN);
            }
        });
    }

    @Override
    public void getMatches(@NonNull final Region region, @NonNull final String playerId,
            @NonNull final ApiListener<MatchesBundle> listener) {
        String url = region.getEndpoint().getMatchesApiPath(region.getId(), playerId);

        mGarPrApi.getMatches(url).enqueue(new Callback<MatchesBundle>() {
            @Override
            public void onResponse(final Call<MatchesBundle> call,
                    final Response<MatchesBundle> response) {
                if (response.isSuccessful()) {
                    listener.success(response.body());
                } else {
                    mTimber.e(TAG, "getMatches (" + region + ") (" + playerId + ") failed (code "
                            + response.code() + ")");
                    listener.failure(response.code());
                }
            }

            @Override
            public void onFailure(final Call<MatchesBundle> call, final Throwable t) {
                mTimber.e(TAG, "getMatches (" + region + ") (" + playerId + ") failed", t);
                listener.failure(Constants.ERROR_CODE_UNKNOWN);
            }
        });
    }

    @Override
    public void getPlayer(@NonNull final Region region, @NonNull final String playerId,
            @NonNull final ApiListener<FullPlayer> listener) {
        String url = region.getEndpoint().getPlayerApiPath(region.getId(), playerId);

        mGarPrApi.getPlayer(url).enqueue(new Callback<FullPlayer>() {
            @Override
            public void onResponse(final Call<FullPlayer> call, final Response<FullPlayer> response) {
                if (response.isSuccessful()) {
                    listener.success(response.body());
                } else {
                    mTimber.e(TAG, "getPlayer (" + region + ") (" + playerId + ") failed (code "
                            + response.code() + ")");
                    listener.failure(response.code());
                }
            }

            @Override
            public void onFailure(final Call<FullPlayer> call, final Throwable t) {
                mTimber.e(TAG, "getPlayer (" + region + ") (" + playerId + ") failed", t);
                listener.failure(Constants.ERROR_CODE_UNKNOWN);
            }
        });
    }

    @Override
    public void getPlayerMatches(@NonNull final Region region, @NonNull final String playerId,
            @NonNull final ApiListener<PlayerMatchesBundle> listener) {
        // TODO
    }

    @Override
    public void getPlayers(@NonNull final Region region,
            @NonNull final ApiListener<PlayersBundle> listener) {
        String url = region.getEndpoint().getPlayersApiPath(region.getId());

        mGarPrApi.getPlayers(url).enqueue(new Callback<PlayersBundle>() {
            @Override
            public void onResponse(final Call<PlayersBundle> call,
                    final Response<PlayersBundle> response) {
                if (response.isSuccessful()) {
                    listener.success(response.body());
                } else {
                    mTimber.e(TAG, "getPlayers (" + region + ") failed (code " + response.code()
                            + ")");
                    listener.failure(response.code());
                }
            }

            @Override
            public void onFailure(final Call<PlayersBundle> call, final Throwable t) {
                mTimber.e(TAG, "getPlayers (" + region + ") failed", t);
                listener.failure(Constants.ERROR_CODE_UNKNOWN);
            }
        });
    }

    @Override
    public void getRankings(@NonNull final Region region,
            @NonNull final ApiListener<RankingsBundle> listener) {
        String url = region.getEndpoint().getRankingsApiPath(region.getId());

        mGarPrApi.getRankings(url).enqueue(new Callback<RankingsBundle>() {
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
                    listener.failure(response.code());
                }
            }

            @Override
            public void onFailure(final Call<RankingsBundle> call, final Throwable t) {
                mTimber.e(TAG, "getRankings (" + region + ") failed", t);
                listener.failure(Constants.ERROR_CODE_UNKNOWN);
            }
        });
    }

    @Override
    public void getRegions(@NonNull final ApiListener<RegionsBundle> listener) {
        Endpoint[] endpoints = Endpoint.values();
        final boolean[] array = new boolean[endpoints.length];
        final RegionsBundle regionsBundle = new RegionsBundle();

        for (int i = 0; i < endpoints.length; ++i) {
            final int index = i;

            getRegions(endpoints[i], new ApiListener<RegionsBundle>() {
                @Override
                public void failure(final int errorCode) {
                    array[index] = true;
                    proceed();
                }

                @Override
                public boolean isAlive() {
                    return listener.isAlive();
                }

                private synchronized void proceed() {
                    for (final boolean completed : array) {
                        if (!completed) {
                            return;
                        }
                    }

                    if (isAlive()) {
                        listener.success(regionsBundle);
                    }
                }

                @Override
                public void success(@Nullable final RegionsBundle object) {
                    regionsBundle.merge(object);
                    array[index] = true;
                    proceed();
                }
            });
        }
    }

    private void getRegions(@NonNull final Endpoint endpoint,
            @NonNull final ApiListener<RegionsBundle> listener) {
        String url = endpoint.getRegionsApiPath();

        mGarPrApi.getRegions(url).enqueue(new Callback<RegionsBundle>() {
            @Override
            public void onResponse(final Call<RegionsBundle> call,
                    final Response<RegionsBundle> response) {
                if (response.isSuccessful()) {
                    final RegionsBundle body = response.body();

                    if (body != null && body.hasRegions()) {
                        // noinspection ConstantConditions
                        for (final Region region : body.getRegions()) {
                            region.setEndpoint(endpoint);
                        }
                    }

                    listener.success(body);
                } else {
                    mTimber.e(TAG, "getRegions failed (code " + response.code() + ")");
                    listener.failure(response.code());
                }
            }

            @Override
            public void onFailure(final Call<RegionsBundle> call, final Throwable t) {
                mTimber.e(TAG, "getRegions failed", t);
                listener.failure(Constants.ERROR_CODE_UNKNOWN);
            }
        });
    }

    @Override
    public void getTournament(@NonNull final Region region, @NonNull final String tournamentId,
            @NonNull final ApiListener<FullTournament> listener) {
        String url = region.getEndpoint().getTournamentApiPath(region.getId(), tournamentId);

        mGarPrApi.getTournament(url).enqueue(new Callback<FullTournament>() {
            @Override
            public void onResponse(final Call<FullTournament> call,
                    final Response<FullTournament> response) {
                if (response.isSuccessful()) {
                    listener.success(response.body());
                } else {
                    mTimber.e(TAG, "getTournament (" + region + ") (" + tournamentId +
                            ") failed (code " + response.code() + ")");
                    listener.failure(response.code());
                }
            }

            @Override
            public void onFailure(final Call<FullTournament> call, final Throwable t) {
                mTimber.e(TAG, "getTournament (" + region + ") (" + tournamentId + ") failed", t);
                listener.failure(Constants.ERROR_CODE_UNKNOWN);
            }
        });
    }

    @Override
    public void getTournaments(@NonNull final Region region,
            @NonNull final ApiListener<TournamentsBundle> listener) {
        String url = region.getEndpoint().getTournamentsApiPath(region.getId());

        mGarPrApi.getTournaments(url).enqueue(new Callback<TournamentsBundle>() {
            @Override
            public void onResponse(final Call<TournamentsBundle> call,
                    final Response<TournamentsBundle> response) {
                if (response.isSuccessful()) {
                    listener.success(response.body());
                } else {
                    mTimber.e(TAG, "getTournaments (" + region + ") failed (code " +
                            response.code() + ")");
                    listener.failure(response.code());
                }
            }

            @Override
            public void onFailure(final Call<TournamentsBundle> call, final Throwable t) {
                mTimber.e(TAG, "getTournaments (" + region + ") failed", t);
                listener.failure(Constants.ERROR_CODE_UNKNOWN);
            }
        });
    }

}
