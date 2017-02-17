package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.R;
import com.garpr.android.fragments.RankingsFragment;

public class RankingsActivity extends BaseActivity {

    private static final String TAG = "RankingsActivity";


    public static Intent getLaunchIntent(final Context context, @NonNull final String region) {
        return new Intent(context, RankingsActivity.class)
                .putExtra(EXTRA_REGION, region);
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flFragment, RankingsFragment.create(), RankingsFragment.TAG)
                .commit();
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

}
