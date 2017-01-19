package com.garpr.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.garpr.android.R;
import com.garpr.android.lifecycle.ActivityState;
import com.garpr.android.lifecycle.ActivityStateHandle;
import com.garpr.android.lifecycle.Heartbeat;
import com.garpr.android.misc.TagHandle;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements ActivityStateHandle,
        Heartbeat, TagHandle {

    private ActivityState mActivityState;
    private Bundle mSavedInstanceState;

    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;


    @Nullable
    @Override
    public ActivityState getActivityState() {
        return mActivityState;
    }

    @Nullable
    @Override
    public Bundle getSavedInstanceState() {
        return mSavedInstanceState;
    }

    @Override
    public boolean isAlive() {
        return !isFinishing() && !isDestroyed();
    }

    private void navigateUp() {
        final Intent upIntent = NavUtils.getParentActivityIntent(this);

        if (upIntent == null) {
            supportFinishAfterTransition();
        } else if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities();
        } else {
            NavUtils.navigateUpTo(this, upIntent);
        }
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityState = ActivityState.CREATED;
        mSavedInstanceState = savedInstanceState;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityState = ActivityState.DESTROYED;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateUp();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActivityState = ActivityState.PAUSED;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivityState = ActivityState.RESUMED;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mActivityState = ActivityState.STARTED;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mActivityState = ActivityState.STOPPED;
    }

    protected void onViewsBound() {
        ButterKnife.bind(this);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    public void setContentView(@LayoutRes final int layoutResID) {
        super.setContentView(layoutResID);
        onViewsBound();
    }

    @Override
    public String toString() {
        return getTag();
    }

}
