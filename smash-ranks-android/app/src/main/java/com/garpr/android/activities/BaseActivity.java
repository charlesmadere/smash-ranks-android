package com.garpr.android.activities;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.garpr.android.R;
import com.garpr.android.misc.Heartbeat;
import com.garpr.android.misc.TagHandle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity implements Heartbeat, TagHandle {

    private Unbinder mUnbinder;

    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;


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
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (showBaseMenu()) {
            getMenuInflater().inflate(R.menu.base_activity, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateUp();
                return true;

            case R.id.miSettings:
                startActivity(SettingsActivity.getLaunchIntent(this));
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
    }

    @Override
    public void setContentView(@LayoutRes final int layoutResID) {
        super.setContentView(layoutResID);
        onViewsBound();
    }

    protected boolean showBaseMenu() {
        return true;
    }

    @Override
    public String toString() {
        return getTag();
    }

}
