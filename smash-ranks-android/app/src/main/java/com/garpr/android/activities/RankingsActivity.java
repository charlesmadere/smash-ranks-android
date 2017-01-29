package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.garpr.android.R;

public class RankingsActivity extends BaseActivity {

    private static final String TAG = "RankingsActivity";


    public static Intent getLaunchIntent(final Context context) {
        return new Intent(context, RankingsActivity.class);
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);
    }

}
