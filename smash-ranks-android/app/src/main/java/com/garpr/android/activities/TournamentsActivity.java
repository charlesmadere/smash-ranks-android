package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;

import com.garpr.android.R;

public class TournamentsActivity extends BaseNavigationActivity {

    private static final String TAG = "TournamentsActivity";


    public static Intent getLaunchIntent(final Context context) {
        return new Intent(context, TournamentsActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    }

    @IdRes
    @Override
    protected int getSelectedNavigationItem() {
        return R.id.actionTournaments;
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

}
