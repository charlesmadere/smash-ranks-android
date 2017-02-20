package com.garpr.android.misc;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.garpr.android.R;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.AbsTournament;

public class ShareUtilsImpl implements ShareUtils {

    private static final String TAG = "ShareUtilsImpl";
    private static final String PLAIN_TEXT = "text/plain";

    private final RegionManager mRegionManager;
    private final String mBaseWebUrl;
    private final Timber mTimber;


    public ShareUtilsImpl(@NonNull final RegionManager regionManager,
            @NonNull final String baseWebUrl, @NonNull final Timber timber) {
        mRegionManager = regionManager;
        mBaseWebUrl = baseWebUrl;
        mTimber = timber;
    }

    @Override
    public void openUrl(@NonNull final Context context, @Nullable final String url) {
        if (TextUtils.isEmpty(url) || TextUtils.getTrimmedLength(url) == 0) {
            return;
        }

        final Uri uri = Uri.parse(url);

        try {
            final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } catch (final ActivityNotFoundException e) {
            mTimber.e(TAG, "Unable to open browser to URI: \"" + uri + "\"", e);
            Toast.makeText(context, R.string.unable_to_open_link, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void sharePlayer(@NonNull final Activity activity, @NonNull final AbsPlayer player) {
        final String region = mRegionManager.getRegion(activity);

        ShareCompat.IntentBuilder.from(activity)
                .setChooserTitle(activity.getString(R.string.share_x, player.getName()))
                .setText(mBaseWebUrl + region + "/players/" + player.getId())
                .setType(PLAIN_TEXT)
                .startChooser();
    }

    @Override
    public void shareTournament(@NonNull final Activity activity,
            @NonNull final AbsTournament tournament) {
        final String region = mRegionManager.getRegion(activity);

        ShareCompat.IntentBuilder.from(activity)
                .setChooserTitle(activity.getString(R.string.share_x, tournament.getName()))
                .setText(mBaseWebUrl + region + "/tournaments/" + tournament.getId())
                .setType(PLAIN_TEXT)
                .startChooser();
    }

}