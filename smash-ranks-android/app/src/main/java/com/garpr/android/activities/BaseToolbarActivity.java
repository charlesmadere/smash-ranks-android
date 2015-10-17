package com.garpr.android.activities;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.garpr.android.R;
import com.garpr.android.settings.Settings;

import butterknife.Bind;


public abstract class BaseToolbarActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener {


    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private ActionBarDrawerToggle mDrawerToggle;




    protected void closeDrawer() {
        mDrawerLayout.closeDrawer(mNavigationView);
    }


    protected int getOptionsMenu() {
        return 0;
    }


    protected int getSelectedNavigationItemId() {
        return 0;
    }


    protected Toolbar getToolbar() {
        return mToolbar;
    }


    private void initializeNavigationDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(final View drawerView) {
                super.onDrawerClosed(drawerView);
                BaseToolbarActivity.this.onDrawerClosed();
            }


            @Override
            public void onDrawerOpened(final View drawerView) {
                super.onDrawerOpened(drawerView);
                BaseToolbarActivity.this.onDrawerOpened();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setStatusBarBackgroundColor(getColorCompat(R.color.gray_dark));

        if (showDrawerIndicator()) {
            mDrawerToggle.setDrawerIndicatorEnabled(true);
        } else {
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            mToolbar.setNavigationIcon(R.drawable.ic_back);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    navigateUp();
                }
            });
        }

        mNavigationView.setNavigationItemSelectedListener(this);

        if (Settings.User.Player.exists()) {
            final Menu menu = mNavigationView.getMenu();
            final MenuItem profile = menu.findItem(R.id.navigation_view_menu_profile);
            profile.setVisible(true);
        }
    }


    protected boolean isDrawerClosed() {
        return !isDrawerOpen();
    }


    protected boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mNavigationView);
    }


    protected boolean isDrawerVisible() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerVisible(mNavigationView);
    }


    @Override
    public void onBackPressed() {
        if (isDrawerVisible()) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);
        initializeNavigationDrawer();
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final int menuRes = getOptionsMenu();

        if (menuRes != 0) {
            final MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(menuRes, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }


    protected void onDrawerClosed() {
        // this method intentionally left blank (children can override)
    }


    protected void onDrawerOpened() {
        // this method intentionally left blank (children can override)
    }


    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        IntentBuilder intentBuilder = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_view_menu_about:
                intentBuilder = new AboutActivity.IntentBuilder(this);
                break;

            case R.id.navigation_view_menu_profile:
                intentBuilder = new PlayerActivity.IntentBuilder(this, Settings.User.Player.get());
                break;

            case R.id.navigation_view_menu_rankings:
                intentBuilder = new RankingsActivity.IntentBuilder(this);
                break;

            case R.id.navigation_view_menu_settings:
                intentBuilder = new SettingsActivity.IntentBuilder(this);
                break;

            case R.id.navigation_view_menu_tournaments:
                intentBuilder = new TournamentsActivity.IntentBuilder(this);
                break;
        }

        if (intentBuilder == null) {
            return false;
        } else {
            intentBuilder.start(this);
            closeDrawer();
            return true;
        }
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final boolean actionConsumed;

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            actionConsumed = true;
        } else {
            actionConsumed = super.onOptionsItemSelected(item);
        }

        return actionConsumed;
    }


    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    protected void onResume() {
        super.onResume();

        final Menu menu = mNavigationView.getMenu();
        final int selectedNavigationItemId = getSelectedNavigationItemId();

        for (int i = 0; i < menu.size(); ++i) {
            final MenuItem mi = menu.getItem(i);
            mi.setChecked(mi.getItemId() == selectedNavigationItemId);
        }
    }


    protected boolean showDrawerIndicator() {
        return true;
    }


}
