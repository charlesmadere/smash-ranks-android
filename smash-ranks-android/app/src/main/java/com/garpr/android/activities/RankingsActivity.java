package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.models.Region;

import javax.inject.Inject;

public class RankingsActivity extends BaseJavaActivity {

    private static final String TAG = "RankingsActivity";

    @Inject
    RegionManager mRegionManager;


    public static Intent getLaunchIntent(final Context context) {
        return getLaunchIntent(context, null);
    }

    public static Intent getLaunchIntent(final Context context, @Nullable final Region region) {
        final Intent intent = new Intent(context, RankingsActivity.class);

        if (region != null) {
            intent.putExtra(EXTRA_REGION, region);
        }

        return intent;
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);
        setContentView(R.layout.activity_rankings);
        setSubtitle(mRegionManager.getRegion(this).getDisplayName());
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

}
