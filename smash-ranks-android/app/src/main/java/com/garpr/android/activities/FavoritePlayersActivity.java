package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.FavoritePlayersManager;

import javax.inject.Inject;

public class FavoritePlayersActivity extends BaseActivity implements
        FavoritePlayersManager.OnFavoritePlayersChangeListener {

    private static final String TAG = "FavoritePlayersActivity";

    @Inject
    FavoritePlayersManager mFavoritePlayersManager;


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
        App.get().getAppComponent().inject(this);
        setContentView(R.layout.activity_favorite_players);

        mFavoritePlayersManager.addListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFavoritePlayersManager.removeListener(this);
    }

    @Override
    public void onFavoritePlayersChanged(final FavoritePlayersManager manager) {
        // TODO
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

}
