package com.garpr.android.calls;


import com.garpr.android.misc.Console;
import com.garpr.android.misc.Constants;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.models.RankingsBundle.DateWrapper;
import com.garpr.android.models.Region;
import com.garpr.android.settings.Settings;
import com.garpr.android.settings.Settings.User;

import org.json.JSONException;
import org.json.JSONObject;


public final class Rankings {


    public static void checkForUpdates(final Response<Result> response) {
        new CheckForRankingsUpdatesCall(response).make();
    }


    public static void get(final Response<RankingsBundle> response, final boolean ignoreCache) {
        new RankingsCall(response, ignoreCache).make();
    }




    private static abstract class BaseRankingsCall<T> extends RegionBasedCall<T> {


        BaseRankingsCall(final Response<T> response, final boolean ignoreCache, final Region region)
                throws IllegalArgumentException {
            super(response, ignoreCache, region);
        }


        @Override
        final String getUrl() {
            return super.getUrl() + Constants.RANKINGS;
        }


        @Override
        final void onJSONResponse(final JSONObject json) throws JSONException {
            final RankingsBundle rankingsBundle = new RankingsBundle(json);

            if (rankingsBundle.hasDateWrapper()) {
                final DateWrapper dateWrapper = rankingsBundle.getDateWrapper();
                final long newRankingsDate = dateWrapper.getDate().getTime();
                Settings.RankingsDate.set(newRankingsDate);
            } else {
                Console.w(getCallName(), "RankingsBundle has no DateWrapper? Region is "
                        + Settings.Region.get().getName());
            }

            onRankingsBundleResponse(rankingsBundle);
        }


        abstract void onRankingsBundleResponse(final RankingsBundle rankingsBundle);


    }


    private static final class CheckForRankingsUpdatesCall extends BaseRankingsCall<Result> {


        private static final String TAG = "UpdateCall";

        private final long mCurrentRankingsDate;


        private CheckForRankingsUpdatesCall(final Response<Result> response)
                throws IllegalArgumentException {
            super(response, true, User.Region.get());
            mCurrentRankingsDate = Settings.RankingsDate.get();
        }


        @Override
        String getCallName() {
            return TAG;
        }


        @Override
        void onRankingsBundleResponse(final RankingsBundle rankingsBundle) {
            final long newRankingsDate = Settings.RankingsDate.get();

            if (mCurrentRankingsDate != 0L && mCurrentRankingsDate < newRankingsDate) {
                mResponse.success(Result.UPDATE_AVAILABLE);
            } else {
                mResponse.success(Result.NO_UPDATE);
            }
        }


    }


    private static final class RankingsCall extends BaseRankingsCall<RankingsBundle> {


        private static final String TAG = "RankingsCall";


        private RankingsCall(final Response<RankingsBundle> response, final boolean ignoreCache)
                throws IllegalArgumentException {
            super(response, ignoreCache, Settings.Region.get());
        }


        @Override
        String getCallName() {
            return TAG;
        }


        @Override
        void onRankingsBundleResponse(final RankingsBundle rankingsBundle) {
            mResponse.success(rankingsBundle);
        }


    }


    public enum Result {
        NO_UPDATE, UPDATE_AVAILABLE
    }


}
