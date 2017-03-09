package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.RegionManager;

import javax.inject.Inject;

public class PlayersActivity extends BaseActivity {

    private static final String TAG = "PlayersActivity";

    @Inject
    RegionManager mRegionManager;


    public static Intent getLaunchIntent(final Context context) {
        return getLaunchIntent(context, null);
    }

    public static Intent getLaunchIntent(final Context context, @Nullable final String region) {
        final Intent intent = new Intent(context, PlayersActivity.class);

        if (!TextUtils.isEmpty(region)) {
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
        setContentView(R.layout.activity_players);
        setSubtitle(mRegionManager.getRegion(this));
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

}
