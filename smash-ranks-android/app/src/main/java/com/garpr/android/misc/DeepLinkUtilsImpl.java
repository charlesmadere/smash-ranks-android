package com.garpr.android.misc;

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
import com.garpr.android.models.Endpoint;
import com.garpr.android.models.Region;
import com.garpr.android.models.RegionsBundle;

import java.util.ArrayList;
import java.util.List;

public class DeepLinkUtilsImpl implements DeepLinkUtils {

    private static final String TAG = "DeepLinkUtilsImpl";

    // Players
    // https://www.garpr.com/#/norcal/players
    // https://www.notgarpr.com/#/newjersey/players

    // Rankings
    // https://www.garpr.com/#/norcal/rankings
    // https://www.notgarpr.com/#/nyc/rankings

    // apollo iii
    // https://www.notgarpr.com/#/nyc/tournaments/58c72c801d41c8259fa1f8bf

    // Norcal Validated 2
    // https://www.garpr.com/#/norcal/tournaments/58a00514d2994e4d0f2e25a6

    // rubicon 12
    // https://www.notgarpr.com/#/chicago/tournaments/579839b0e592573cf1845f46

    // SFAT
    // https://www.garpr.com/#/norcal/players/588852e8d2994e3bbfa52d88

    // Swedish Delight
    // https://www.notgarpr.com/#/nyc/players/545b240b8ab65f7a95f74940

    private static final String PLAYERS = "players";
    private static final String RANKINGS = "rankings";
    private static final String TOURNAMENTS = "tournaments";

    private final RegionManager mRegionManager;
    private final Timber mTimber;


    public DeepLinkUtilsImpl(@NonNull final RegionManager regionManager,
            @NonNull final Timber timber) {
        mRegionManager = regionManager;
        mTimber = timber;
    }

    @Nullable
    @Override
    public Intent[] buildIntentStack(@NonNull final Context context,
            @Nullable final Intent intent, @NonNull final Region region) {
        if (intent == null) {
            mTimber.d(TAG, "Can't deep link, Intent is null");
            return null;
        } else {
            return buildIntentStack(context, intent.getData(), region);
        }
    }

    @Nullable
    @Override
    public Intent[] buildIntentStack(@NonNull final Context context, @Nullable final String uri,
            @NonNull final Region region) {
        if (TextUtils.isEmpty(uri) || TextUtils.getTrimmedLength(uri) == 0) {
            mTimber.d(TAG, "Can't deep link, uri is null / empty / whitespace");
            return null;
        }

        mTimber.d(TAG, "Attempting to deep link to \"" + uri + "\"");

        final Endpoint endpoint = getEndpoint(uri);

        if (endpoint == null) {
            mTimber.e(TAG, "Deep link path isn't for GAR PR");
            return null;
        }

        final String path = uri.substring(endpoint.getWebPath().length(), uri.length());

        if (TextUtils.isEmpty(path) || TextUtils.getTrimmedLength(path) == 0) {
            mTimber.d(TAG, "Deep link path is null / empty / whitespace");
            return null;
        }

        final String[] splits = path.split("/");

        if (splits.length == 0) {
            mTimber.d(TAG, "Deep link's path split is empty");
            return null;
        }

        final String regionId = splits[0];

        if (TextUtils.isEmpty(regionId) || TextUtils.getTrimmedLength(regionId) == 0) {
            mTimber.w(TAG, "Region ID is null / empty / whitespace");
            return null;
        }

        final boolean sameRegion = regionId.equalsIgnoreCase(mRegionManager.getRegion().getId());

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
    public Intent[] buildIntentStack(@NonNull final Context context, @Nullable final Uri uri,
            @NonNull final Region region) {
        if (uri == null) {
            mTimber.d(TAG, "Can't deep link, Uri is null");
            return null;
        } else {
            return buildIntentStack(context, uri.toString(), region);
        }
    }

    @Nullable
    private Intent[] buildIntentStack(@Nullable final List<Intent> intentStack) {
        if (intentStack == null || intentStack.isEmpty()) {
            return null;
        }

        mTimber.d(TAG, "Creating Intent stack of size " + intentStack.size());
        final Intent[] intentArray = new Intent[intentStack.size()];
        intentStack.toArray(intentArray);

        return intentArray;
    }

    private void buildPlayersIntentStack(final Context context, final List<Intent> intentStack,
            final Region region, final boolean sameRegion, final String[] splits) {
        intentStack.add(HomeActivity.Companion.getLaunchIntent(context));

        if (sameRegion) {
            intentStack.add(PlayersActivity.Companion.getLaunchIntent(context));
        } else {
            intentStack.add(PlayersActivity.Companion.getLaunchIntent(context, region));
        }

        if (splits.length < 3) {
            return;
        }

        final String playerId = splits[2];

        if (TextUtils.isEmpty(playerId) || TextUtils.getTrimmedLength(playerId) == 0) {
            return;
        }

        intentStack.add(PlayerActivity.Companion.getLaunchIntent(context, playerId, null,
                sameRegion ? null : region));
    }

    private void buildRankingsIntentStack(final Context context, final List<Intent> intentStack,
            final Region region, final boolean sameRegion) {
        if (sameRegion) {
            intentStack.add(HomeActivity.Companion.getLaunchIntent(context,
                    HomeActivity.Companion.getPositionRankings()));
        } else {
            intentStack.add(HomeActivity.Companion.getLaunchIntent(context));
            intentStack.add(RankingsActivity.Companion.getLaunchIntent(context, region));
        }
    }

    private void buildTournamentsIntentStack(final Context context, final List<Intent> intentStack,
            final Region region, final boolean sameRegion, final String[] splits) {
        if (sameRegion) {
            intentStack.add(HomeActivity.Companion.getLaunchIntent(context,
                    HomeActivity.Companion.getPositionTournaments()));
        } else {
            intentStack.add(HomeActivity.Companion.getLaunchIntent(context));
            intentStack.add(TournamentsActivity.Companion.getLaunchIntent(context, region));
        }

        if (splits.length < 3) {
            return;
        }

        final String tournamentId = splits[2];

        if (TextUtils.isEmpty(tournamentId) || TextUtils.getTrimmedLength(tournamentId) == 0) {
            return;
        }

        if (sameRegion) {
            intentStack.add(TournamentActivity.Companion.getLaunchIntent(context, tournamentId,
                    null, null, null));
        } else {
            intentStack.add(TournamentActivity.Companion.getLaunchIntent(context, tournamentId,
                    null, null, region));
        }
    }

    @Nullable
    @Override
    public Endpoint getEndpoint(@Nullable final Intent intent) {
        if (intent == null) {
            return null;
        } else {
            return getEndpoint(intent.getData());
        }
    }

    @Nullable
    @Override
    public Endpoint getEndpoint(@Nullable final String uri) {
        if (TextUtils.isEmpty(uri) || TextUtils.getTrimmedLength(uri) == 0) {
            return null;
        }

        for (final Endpoint endpoint : Endpoint.values()) {
            if (uri.startsWith(endpoint.getBasePath())) {
                return endpoint;
            }
        }

        return null;
    }

    @Nullable
    @Override
    public Endpoint getEndpoint(@Nullable final Uri uri) {
        if (uri == null) {
            return null;
        } else {
            return getEndpoint(uri.toString());
        }
    }

    @Nullable
    @Override
    public Region getRegion(@Nullable final Intent intent,
            @Nullable final RegionsBundle regionsBundle) {
        if (intent == null) {
            return null;
        } else {
            return getRegion(intent.getData(), regionsBundle);
        }
    }

    @Nullable
    @Override
    public Region getRegion(@Nullable String uri, @Nullable final RegionsBundle regionsBundle) {
        if (TextUtils.isEmpty(uri) || TextUtils.getTrimmedLength(uri) == 0 ||
                regionsBundle == null || !regionsBundle.hasRegions()) {
            return null;
        }

        uri = uri.trim();

        // noinspection ConstantConditions
        for (final Region region : regionsBundle.getRegions()) {
            if (uri.startsWith(region.getEndpoint().getWebPath(region.getId()))) {
                return region;
            }
        }

        return null;
    }

    @Nullable
    @Override
    public Region getRegion(@Nullable final Uri uri, @Nullable final RegionsBundle regionsBundle) {
        if (uri == null) {
            return null;
        } else {
            return getRegion(uri.toString(), regionsBundle);
        }
    }

    @Override
    public boolean isValidUri(@Nullable final Intent intent) {
        return intent != null && isValidUri(intent.getData());
    }

    @Override
    public boolean isValidUri(@Nullable final String uri) {
        return getEndpoint(uri) != null;
    }

    @Override
    public boolean isValidUri(@Nullable final Uri uri) {
        return uri != null && isValidUri(uri.toString());
    }

}
