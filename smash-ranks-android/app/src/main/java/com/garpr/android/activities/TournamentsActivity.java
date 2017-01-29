package com.garpr.android.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.garpr.android.R;

public class TournamentsActivity extends BaseActivity {

    private static final String TAG = "TournamentsActivity";


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
