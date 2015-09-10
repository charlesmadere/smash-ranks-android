package com.garpr.android.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.garpr.android.App;
import com.garpr.android.fragments.BaseFragment;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsManager;
import com.garpr.android.misc.HeartbeatWithUi;
import com.garpr.android.misc.NotificationManager;
import com.garpr.android.models.Region;
import com.garpr.android.settings.RegionSetting;
import com.garpr.android.settings.Settings;


/**
 * All Activities should extend from this base class, as it greatly reduces the otherwise
 * necessary boilerplate.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseFragment.Listener,
        HeartbeatWithUi, RegionSetting.OnSettingChangedListener<Region> {


    private boolean mIsAlive;
    private boolean mIsFirstResume;




    public abstract String getActivityName();


    public int getColorCompat(final int colorResId) {
        return ContextCompat.getColor(this, colorResId);
    }


    protected abstract int getContentView();


    @Override
    public boolean isAlive() {
        return mIsAlive;
    }


    protected boolean isFirstResume() {
        return mIsFirstResume;
    }


    /**
     * This method's code was taken from the Android documentation:
     * https://developer.android.com/training/implementing-navigation/ancestral.html
     */
    protected void navigateUp() {
        final Intent upIntent = NavUtils.getParentActivityIntent(this);

        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities();
        } else {
            NavUtils.navigateUpTo(this, upIntent);
        }
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mIsAlive = true;
        mIsFirstResume = true;
        Settings.Region.attachListener(this);
    }


    @Override
    protected void onDestroy() {
        mIsAlive = false;
        App.cancelNetworkRequests(this);
        Settings.Region.detachListener(this);
        super.onDestroy();
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateUp();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        mIsFirstResume = false;
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        CrashlyticsManager.setString(Constants.CURRENT_ACTIVITY, getActivityName());
    }


    protected void onRegionChanged(final Region region) {
        // this method intentionally left blank (children can override)
    }


    @Override
    public final void onSettingChanged(final Region setting) {
        runOnUi(new Runnable() {
            @Override
            public void run() {
                onRegionChanged(setting);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        NotificationManager.clear();
    }


    @Override
    public void runOnUi(final Runnable action) {
        if (isAlive()) {
            runOnUiThread(action);
        }
    }


    @Override
    public String toString() {
        return getActivityName();
    }




    protected static abstract class IntentBuilder {


        protected final Intent mIntent;


        protected IntentBuilder(final Context context, final Class c) {
            mIntent = new Intent(context, c);
        }


        public Intent getIntent() {
            return mIntent;
        }


        public void start(final Activity activity) {
            activity.startActivity(mIntent);
        }


    }


}
