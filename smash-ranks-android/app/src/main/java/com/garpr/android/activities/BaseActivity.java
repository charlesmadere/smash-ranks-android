package com.garpr.android.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.garpr.android.lifecycle.ActivityState;
import com.garpr.android.lifecycle.ActivityStateHandle;
import com.garpr.android.lifecycle.Heartbeat;
import com.garpr.android.misc.TagHandle;

public abstract class BaseActivity extends AppCompatActivity implements ActivityStateHandle,
        Heartbeat, TagHandle {

    private ActivityState mActivityState;
    private Bundle mSavedInstanceState;


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

}
