package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.garpr.android.R;

public class FavoritePlayersActivity extends BaseActivity {

    private static final String TAG = "FavoritePlayersActivity";


    public static Intent getLaunchIntent(final Context context) {
        return new Intent(context, FavoritePlayersActivity.class);
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_players);
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

}
