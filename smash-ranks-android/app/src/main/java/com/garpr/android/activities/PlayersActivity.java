package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.R;
import com.garpr.android.fragments.PlayersFragment;

public class PlayersActivity extends BaseActivity {

    private static final String TAG = "PlayersActivity";


    public static Intent getLaunchIntent(final Context context, @NonNull final String region) {
        return new Intent(context, PlayersActivity.class)
                .putExtra(EXTRA_REGION, region);
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flFragment, PlayersFragment.create(), PlayersFragment.TAG)
                .commit();
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

}