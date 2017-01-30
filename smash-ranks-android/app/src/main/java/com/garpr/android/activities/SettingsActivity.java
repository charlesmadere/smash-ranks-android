package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.preferences.PreferenceStore;

import javax.inject.Inject;

public class SettingsActivity extends BaseActivity {

    private static final String TAG = "SettingsActivity";

    @Inject
    PreferenceStore mPreferenceStore;


    public static Intent getLaunchIntent(final Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    public String getActivityName() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().getAppComponent().inject(this);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

}
