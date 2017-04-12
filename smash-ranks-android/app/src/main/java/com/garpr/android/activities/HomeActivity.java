package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.HomePagerAdapter;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.NotificationManager;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.SearchQueryHandle;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.models.Region;
import com.garpr.android.sync.RankingsPollingSyncManager;
import com.garpr.android.views.RankingsLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnPageChange;

public class HomeActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        IdentityManager.OnIdentityChangeListener, MenuItemCompat.OnActionExpandListener,
        RankingsLayout.Listener, RegionManager.OnRegionChangeListener, SearchQueryHandle,
        SearchView.OnQueryTextListener {

    private static final String TAG = "HomeActivity";
    private static final String CNAME = HomeActivity.class.getCanonicalName();
    private static final String EXTRA_INITIAL_POSITION = CNAME + ".InitialPosition";
    private static final String KEY_CURRENT_POSITION = "CurrentPosition";

    public static final int POSITION_RANKINGS = 0;
    public static final int POSITION_TOURNAMENTS = 1;
    public static final int POSITION_FAVORITE_PLAYERS = 2;

    private HomePagerAdapter mAdapter;
    private MenuItem mSearchMenuItem;
    private SearchView mSearchView;

    @Inject
    IdentityManager mIdentityManager;

    @Inject
    NotificationManager mNotificationManager;

    @Inject
    RankingsPollingSyncManager mRankingsPollingSyncManager;

    @Inject
    RegionManager mRegionManager;

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView mBottomNavigationView;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;


    public static Intent getLaunchIntent(final Context context) {
        return new Intent(context, HomeActivity.class);
    }

    public static Intent getLaunchIntent(final Context context, final int initialPosition) {
        return new Intent(context, HomeActivity.class)
                .putExtra(EXTRA_INITIAL_POSITION, initialPosition);
    }

    public static Intent getRestartLaunchIntent(final Context context) {
        final Intent intent = new Intent(context, HomeActivity.class);
        return IntentCompat.makeRestartActivityTask(intent.getComponent());
    }

    @Override
    public String getActivityName() {
        return TAG;
    }

    @Nullable
    @Override
    public CharSequence getSearchQuery() {
        return mSearchView == null ? null : mSearchView.getQuery();
    }

    @Override
    public void onBackPressed() {
        if (mSearchMenuItem != null && MenuItemCompat.isActionViewExpanded(mSearchMenuItem)) {
            MenuItemCompat.collapseActionView(mSearchMenuItem);
            return;
        }

        if (mViewPager != null && mViewPager.getCurrentItem() != 0) {
            mViewPager.setCurrentItem(0, false);
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);
        setContentView(R.layout.activity_home);

        mRankingsPollingSyncManager.enableOrDisable();
        mNotificationManager.cancelAll();

        setInitialPosition(savedInstanceState);

        mIdentityManager.addListener(this);
        mRegionManager.addListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);

        if (!TextUtils.isEmpty(getSubtitle())) {
            mSearchMenuItem = menu.findItem(R.id.miSearch);
            mSearchMenuItem.setVisible(true);

            MenuItemCompat.setOnActionExpandListener(mSearchMenuItem, this);
            mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);
            mSearchView.setQueryHint(getText(R.string.search_));
            mSearchView.setOnQueryTextListener(this);
        }

        if (mIdentityManager.hasIdentity()) {
            menu.findItem(R.id.miViewYourself).setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIdentityManager.removeListener(this);
        mRegionManager.removeListener(this);
    }

    @Override
    public void onIdentityChange(final IdentityManager identityManager) {
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onMenuItemActionCollapse(final MenuItem item) {
        mAdapter.search(null);
        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(final MenuItem item) {
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        if (item.isChecked()) {
            return false;
        }

        switch (item.getItemId()) {
            case R.id.actionFavoritePlayers:
                mViewPager.setCurrentItem(POSITION_FAVORITE_PLAYERS, false);
                return true;

            case R.id.actionRankings:
                mViewPager.setCurrentItem(POSITION_RANKINGS, false);
                return true;

            case R.id.actionTournaments:
                mViewPager.setCurrentItem(POSITION_TOURNAMENTS, false);
                return true;

            default:
                throw new RuntimeException("unknown item: " + item.getTitle());
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miPlayers:
                startActivity(PlayersActivity.getLaunchIntent(this));
                return true;

            case R.id.miSettings:
                startActivity(SettingsActivity.getLaunchIntent(this));
                return true;

            case R.id.miViewYourself:
                // noinspection ConstantConditions
                startActivity(PlayerActivity.getLaunchIntent(this, mIdentityManager.getIdentity(),
                        mRegionManager.getRegion(this)));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnPageChange(R.id.viewPager)
    void onPageChange() {
        updateSelectedBottomNavigationItem();
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        mAdapter.search(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        mAdapter.search(query);
        return false;
    }

    @Override
    public void onRankingsBundleFetched(final RankingsLayout layout) {
        final Region region = mRegionManager.getRegion(this);
        setTitle(region.getEndpoint().getName());

        final RankingsBundle rankingsBundle = layout.getRankingsBundle();

        if (rankingsBundle == null) {
            setSubtitle("");
        } else {
            setSubtitle(getString(R.string.x_updated_y, region.getDisplayName(),
                    rankingsBundle.getTime().getShortForm()));
        }

        supportInvalidateOptionsMenu();
    }

    @Override
    public void onRegionChange(final RegionManager regionManager) {
        final Region region = mRegionManager.getRegion(this);
        setTitle(region.getEndpoint().getName());

        if (mSearchMenuItem != null && MenuItemCompat.isActionViewExpanded(mSearchMenuItem)) {
            MenuItemCompat.collapseActionView(mSearchMenuItem);
        }

        MiscUtils.closeKeyboard(this);

        if (mAdapter != null) {
            mAdapter.refresh();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_POSITION, mViewPager.getCurrentItem());
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();

        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.root_padding));
        mViewPager.setOffscreenPageLimit(3);

        mAdapter = new HomePagerAdapter();
        mViewPager.setAdapter(mAdapter);
    }

    private void setInitialPosition(@Nullable final Bundle savedInstanceState) {
        int initialPosition = -1;

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            initialPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, -1);
        }

        if (initialPosition == -1) {
            final Intent intent = getIntent();

            if (intent != null && intent.hasExtra(EXTRA_INITIAL_POSITION)) {
                initialPosition = intent.getIntExtra(EXTRA_INITIAL_POSITION, -1);
            }
        }

        if (initialPosition != -1) {
            mViewPager.setCurrentItem(initialPosition);
        }
    }

    private void updateSelectedBottomNavigationItem() {
        switch (mViewPager.getCurrentItem()) {
            case POSITION_FAVORITE_PLAYERS:
                mBottomNavigationView.getMenu().findItem(R.id.actionFavoritePlayers).setChecked(true);
                break;

            case POSITION_RANKINGS:
                mBottomNavigationView.getMenu().findItem(R.id.actionRankings).setChecked(true);
                break;

            case POSITION_TOURNAMENTS:
                mBottomNavigationView.getMenu().findItem(R.id.actionTournaments).setChecked(true);
                break;

            default:
                throw new RuntimeException("unknown current item: " + mViewPager.getCurrentItem());
        }
    }

}
