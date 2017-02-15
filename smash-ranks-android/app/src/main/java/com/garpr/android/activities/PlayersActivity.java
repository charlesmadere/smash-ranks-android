package com.garpr.android.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.garpr.android.R;

public class PlayersActivity extends BaseActivity {

    private static final String TAG = "PlayersActivity";


    

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

}
