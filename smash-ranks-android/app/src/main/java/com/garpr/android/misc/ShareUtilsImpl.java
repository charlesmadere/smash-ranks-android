package com.garpr.android.misc;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;

import com.garpr.android.R;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.AbsTournament;

public class ShareUtilsImpl implements ShareUtils {

    private static final String PLAIN_TEXT = "text/plain";

    private final RegionManager mRegionManager;
    private final String mBaseWebUrl;


    public ShareUtilsImpl(@NonNull final RegionManager regionManager,
            @NonNull final String baseWebUrl) {
        mRegionManager = regionManager;
        mBaseWebUrl = baseWebUrl;
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
