package com.garpr.android.misc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.garpr.android.activities.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class DeepLinkUtilsImpl implements DeepLinkUtils {

    private static final String TAG = "DeepLinkUtilsImpl";

    // norcal Players
    // https://www.garpr.com/#/norcal/players

    // norcal Rankings
    // https://www.garpr.com/#/norcal/rankings

    // Norcal Validated 2
    // https://www.garpr.com/#/norcal/tournaments/58a00514d2994e4d0f2e25a6

    // SFAT
    // https://www.garpr.com/#/norcal/players/588852e8d2994e3bbfa52d88

    private static final String PLAYERS = "players";
    private static final String RANKINGS = "rankings";
    private static final String TOURNAMENTS = "tournaments";

    private final RegionManager mRegionManager;
    private final String mGarPrUrl;
    private final Timber mTimber;


    public DeepLinkUtilsImpl(@NonNull final RegionManager regionManager,
            @NonNull final String garPrUrl, @NonNull final Timber timber) {
        mRegionManager = regionManager;
        mGarPrUrl = garPrUrl;
        mTimber = timber;
    }

    @Nullable
    @Override
    public Intent[] buildIntentStack(@NonNull final Activity activity) {
        return buildIntentStack(activity, activity.getIntent());
    }

    @Nullable
    @Override
    public Intent[] buildIntentStack(@NonNull final Context context,
            @Nullable final Intent intent) {
        if (intent == null) {
            mTimber.d(TAG, "Can't deep link, Intent is null");
            return null;
        } else {
            return buildIntentStack(context, intent.getData());
        }
    }

    @Nullable
    @Override
    public Intent[] buildIntentStack(@NonNull final Context context, @Nullable final String uri) {
        if (TextUtils.isEmpty(uri) || TextUtils.getTrimmedLength(uri) == 0) {
            mTimber.d(TAG, "Can't deep link, uri is null / empty / whitespace");
            return null;
        }

        mTimber.d(TAG, "Attempting to deep link to \"" + uri + "\", current region is \"" +
                mRegionManager.getRegion() + "\"");

        final String path;

        if (uri.startsWith(mGarPrUrl)) {
            path = uri.substring(mGarPrUrl.length(), uri.length());
        } else {
            mTimber.w(TAG, "Deep link path isn't for GAR PR");
            return null;
        }

        if (TextUtils.isEmpty(path) || TextUtils.getTrimmedLength(path) == 0) {
            mTimber.d(TAG, "Deep link path is null / empty / whitespace");
            return null;
        }

        final String[] splits = path.split("/");

        if (splits.length == 0) {
            mTimber.d(TAG, "Deep link's path split is empty");
            return null;
        }

        final String bang = splits[0];

        if (!"#".equals(bang)) {
            mTimber.w(TAG, "First path split is not a bang");
            return null;
        }

        if (splits.length == 1) {
            return null;
        }

        final String region = splits[1];

        if (TextUtils.isEmpty(region) || TextUtils.getTrimmedLength(region) == 0) {
            mTimber.w(TAG, "Region is null / empty / whitespace");
            return null;
        }

        final boolean sameRegion = region.equalsIgnoreCase(mRegionManager.getRegion());

        if (sameRegion && splits.length == 2) {
            return null;
        }

        final List<Intent> intentStack = new ArrayList<>();
        intentStack.add(HomeActivity.getLaunchIntent(context));

        final String page = splits[2];

        if (PLAYERS.equalsIgnoreCase(page)) {
            buildPlayersIntentStack(intentStack, splits, region, sameRegion);
        } else if (RANKINGS.equalsIgnoreCase(page)) {
            buildRankingsIntentStack(intentStack, splits, region, sameRegion);
        } else if (TOURNAMENTS.equalsIgnoreCase(page)) {
            buildTournamentsIntentStack(intentStack, splits, region, sameRegion);
        } else {
            mTimber.w(TAG, "Unknown page \"" + page + "\"");
        }

        return buildIntentStack(intentStack);
    }

    @Nullable
    @Override
    public Intent[] buildIntentStack(@NonNull final Context context, @Nullable final Uri uri) {
        if (uri == null) {
            mTimber.d(TAG, "Can't deep link, Uri is null");
            return null;
        } else {
            return buildIntentStack(context, uri.toString());
        }
    }

    @NonNull
    private Intent[] buildIntentStack(@NonNull final List<Intent> intentStack) {
        mTimber.d(TAG, "Creating Intent stack of size " + intentStack.size());
        final Intent[] array = new Intent[intentStack.size()];
        intentStack.toArray(array);
        return array;
    }

    private void buildPlayersIntentStack(@NonNull final List<Intent> intentStack,
            @NonNull final String[] splits, @NonNull final String region, final boolean sameRegion) {
        // TODO
    }

    private void buildRankingsIntentStack(@NonNull final List<Intent> intentStack,
            @NonNull final String[] splits, @NonNull final String region, final boolean sameRegion) {
        // TODO
    }

    private void buildTournamentsIntentStack(@NonNull final List<Intent> intentStack,
            @NonNull final String[] splits, @NonNull final String region, final boolean sameRegion) {
        // TODO
    }

}
