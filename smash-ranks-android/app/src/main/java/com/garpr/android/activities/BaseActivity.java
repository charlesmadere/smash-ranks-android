package com.garpr.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.Heartbeat;
import com.garpr.android.misc.RegionManager.RegionHandle;
import com.garpr.android.misc.Timber;
import com.garpr.android.preferences.GeneralPreferenceStore;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity implements Heartbeat, RegionHandle {

    private static final String TAG = "BaseActivity";
    private static final String CNAME = BaseActivity.class.getCanonicalName();
    protected static final String EXTRA_REGION = CNAME + ".Region";

    private Unbinder mUnbinder;

    @Inject
    protected GeneralPreferenceStore mGeneralPreferenceStore;

    @Inject
    protected Timber mTimber;

    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;


    protected abstract String getActivityName();

    @Nullable
    @Override
    public String getCurrentRegion() {
        final Intent intent = getIntent();

        if (intent != null && intent.hasExtra(EXTRA_REGION)) {
            return intent.getStringExtra(EXTRA_REGION);
        } else {
            return null;
        }
    }

    @Nullable
    protected CharSequence getSubtitle() {
        final ActionBar actionBar = getSupportActionBar();
        return actionBar == null ? null : actionBar.getSubtitle();
    }

    @Override
    public boolean isAlive() {
        return !isFinishing() && !isDestroyed();
    }

    protected void navigateUp() {
        final Intent upIntent = NavUtils.getParentActivityIntent(this);

        if (upIntent == null) {
            supportFinishAfterTransition();
        } else if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities();
        } else {
            supportNavigateUpTo(upIntent);
        }
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        App.get().getAppComponent().inject(this);
        // noinspection ConstantConditions
        getDelegate().setLocalNightMode(mGeneralPreferenceStore.getNightMode().get().getThemeValue());

        super.onCreate(savedInstanceState);
        mTimber.d(TAG, getActivityName() + " created");
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

    protected void onViewsBound() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }

        mUnbinder = ButterKnife.bind(this);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(showUpNavigation());
        }
    }

    @Override
    public void setContentView(@LayoutRes final int layoutResID) {
        super.setContentView(layoutResID);
        onViewsBound();
    }

    public void setSubtitle(@Nullable final CharSequence subtitle) {
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setSubtitle(subtitle);
        }
    }

    public void setSubtitle(@StringRes final int resId) {
        setSubtitle(getText(resId));
    }

    protected boolean showUpNavigation() {
        return false;
    }

    @Override
    public String toString() {
        return getActivityName();
    }

}
