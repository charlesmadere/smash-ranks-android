package com.garpr.android.activities;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.garpr.android.data.Settings;
import com.garpr.android.fragments.FloatingActionButtonRegionsFragment;
import com.garpr.android.fragments.RegionsFragment;


public class RegionsActivity extends BaseFragmentActivity implements
        FloatingActionButtonRegionsFragment.SaveListener {


    private static final String TAG = "RegionsActivity";




    public static void start(final Activity activity) {
        final Intent intent = new Intent(activity, RegionsActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected Fragment createFragment() {
        return FloatingActionButtonRegionsFragment.create();
    }


    @Override
    protected String getActivityName() {
        return TAG;
    }


    @Override
    public void onRegionSaved() {
        final RegionsFragment fragment = (RegionsFragment) getFragment();
        Settings.Region.set(fragment.getSelectedRegion());
        finish();
    }


    @Override
    protected boolean showDrawerIndicator() {
        return false;
    }


}
