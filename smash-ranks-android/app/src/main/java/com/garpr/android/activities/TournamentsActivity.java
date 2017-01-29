package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.garpr.android.R;

public class TournamentsActivity extends BaseNavigationActivity {

    private static final String TAG = "TournamentsActivity";


    public static Intent getLaunchIntent(final Context context) {
        return new Intent(context, TournamentsActivity.class);
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournaments);
    }

    @Override
    protected void onTournamentsSelected() {

    }

}
