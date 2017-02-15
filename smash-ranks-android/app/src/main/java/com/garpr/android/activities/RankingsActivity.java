package com.garpr.android.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.garpr.android.R;

public class RankingsActivity extends BaseActivity {

    private static final String TAG = "RankingsActivity";


    

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

}
