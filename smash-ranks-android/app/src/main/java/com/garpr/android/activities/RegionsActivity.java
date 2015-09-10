package com.garpr.android.activities;


import android.content.Context;
import android.support.v4.app.Fragment;

import com.garpr.android.fragments.FloatingActionButtonRegionsFragment;
import com.garpr.android.fragments.RegionsFragment;
import com.garpr.android.models.Region;
import com.garpr.android.settings.Settings;


public class RegionsActivity extends BaseFragmentActivity implements
        FloatingActionButtonRegionsFragment.SaveListener {


    private static final String TAG = "RegionsActivity";




    @Override
    protected Fragment createFragment() {
        return FloatingActionButtonRegionsFragment.create();
    }


    @Override
    public String getActivityName() {
        return TAG;
    }


    @Override
    public void onRegionSaved() {
        final RegionsFragment fragment = (RegionsFragment) getFragment();
        final Region region = fragment.getSelectedRegion();
        Settings.Region.set(region, true);
        finish();
    }


    @Override
    protected boolean showDrawerIndicator() {
        return false;
    }




    public static class IntentBuilder extends BaseActivity.IntentBuilder {


        public IntentBuilder(final Context context) {
            super(context, RegionsActivity.class);
        }


    }


}
