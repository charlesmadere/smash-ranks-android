package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.fragments.TournamentsFragment;
import com.garpr.android.misc.RegionManager;

import javax.inject.Inject;

public class TournamentsActivity extends BaseActivity {

    private static final String TAG = "TournamentsActivity";

    @Inject
    RegionManager mRegionManager;


    public static Intent getLaunchIntent(final Context context, @NonNull final String region) {
        return new Intent(context, TournamentsActivity.class)
                .putExtra(EXTRA_REGION, region);
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);
        setContentView(R.layout.activity_tournaments);

        setSubtitle(mRegionManager.getRegion(this));

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flFragment, TournamentsFragment.create(), TournamentsFragment.TAG)
                .commit();
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

}
