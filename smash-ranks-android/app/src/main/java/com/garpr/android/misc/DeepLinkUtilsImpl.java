package com.garpr.android.misc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.garpr.android.activities.HomeActivity;
import com.garpr.android.activities.PlayerActivity;
import com.garpr.android.activities.PlayersActivity;
import com.garpr.android.activities.RankingsActivity;
import com.garpr.android.activities.TournamentActivity;
import com.garpr.android.activities.TournamentsActivity;

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

        final String region = splits[0];

        if (TextUtils.isEmpty(region) || TextUtils.getTrimmedLength(region) == 0) {
            mTimber.w(TAG, "Region is null / empty / whitespace");
            return null;
        }

        final boolean sameRegion = region.equalsIgnoreCase(mRegionManager.getRegion());

        if (sameRegion && splits.length == 1) {
            return null;
        }

        final List<Intent> intentStack = new ArrayList<>();
        final String page = splits[1];

        if (PLAYERS.equalsIgnoreCase(page)) {
            buildPlayersIntentStack(context, intentStack, region, sameRegion, splits);
        } else if (RANKINGS.equalsIgnoreCase(page)) {
            buildRankingsIntentStack(context, intentStack, region, sameRegion);
        } else if (TOURNAMENTS.equalsIgnoreCase(page)) {
            buildTournamentsIntentStack(context, intentStack, region, sameRegion, splits);
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

    @Nullable
    private Intent[] buildIntentStack(@Nullable final List<Intent> intentStack) {
        if (intentStack == null || intentStack.isEmpty()) {
            return null;
        }

        mTimber.d(TAG, "Creating Intent stack of size " + intentStack.size());
        final Intent[] array = new Intent[intentStack.size()];
        intentStack.toArray(array);

        return array;
    }

    private void buildPlayersIntentStack(final Context context, final List<Intent> intentStack,
            final String region, final boolean sameRegion, final String[] splits) {
        if (sameRegion) {
            intentStack.add(HomeActivity.getLaunchIntent(context, HomeActivity.POSITION_PLAYERS));
        } else {
            intentStack.add(HomeActivity.getLaunchIntent(context));
            intentStack.add(PlayersActivity.getLaunchIntent(context, region));
        }

        if (splits.length < 3) {
            return;
        }

        final String playerId = splits[2];

        if (TextUtils.isEmpty(playerId) || TextUtils.getTrimmedLength(playerId) == 0) {
            return;
        }

        if (sameRegion) {
            intentStack.add(PlayerActivity.getLaunchIntent(context, playerId, null));
        } else {
            intentStack.add(PlayerActivity.getLaunchIntent(context, playerId, null, region));
        }
    }

    private void buildRankingsIntentStack(final Context context, final List<Intent> intentStack,
            final String region, final boolean sameRegion) {
        if (sameRegion) {
            intentStack.add(HomeActivity.getLaunchIntent(context, HomeActivity.POSITION_RANKINGS));
        } else {
            intentStack.add(HomeActivity.getLaunchIntent(context));
            intentStack.add(RankingsActivity.getLaunchIntent(context, region));
        }
    }

    private void buildTournamentsIntentStack(final Context context, final List<Intent> intentStack,
            final String region, final boolean sameRegion, final String[] splits) {
        if (sameRegion) {
            intentStack.add(HomeActivity.getLaunchIntent(context, HomeActivity.POSITION_TOURNAMENTS));
        } else {
            intentStack.add(HomeActivity.getLaunchIntent(context));
            intentStack.add(TournamentsActivity.getLaunchIntent(context, region));
        }

        if (splits.length < 3) {
            return;
        }

        final String tournamentId = splits[2];

        if (TextUtils.isEmpty(tournamentId) || TextUtils.getTrimmedLength(tournamentId) == 0) {
            return;
        }

        if (sameRegion) {
            intentStack.add(TournamentActivity.getLaunchIntent(context, tournamentId, null, null));
        } else {
            intentStack.add(TournamentActivity.getLaunchIntent(context, tournamentId, null, null,
                    region));
        }
    }

}
