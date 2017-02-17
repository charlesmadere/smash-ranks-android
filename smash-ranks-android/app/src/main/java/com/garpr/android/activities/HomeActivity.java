package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.HomeFragmentAdapter;
import com.garpr.android.fragments.RankingsFragment;
import com.garpr.android.misc.NotificationManager;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.sync.RankingsPollingSyncManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnPageChange;

public class HomeActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener, RankingsFragment.Listener {

    private static final String TAG = "HomeActivity";
    private static final String CNAME = HomeActivity.class.getCanonicalName();
    private static final String EXTRA_INITIAL_POSITION = CNAME + ".InitialPosition";
    private static final String KEY_CURRENT_POSITION = "CurrentPosition";

    public static final int POSITION_RANKINGS = 0;
    public static final int POSITION_TOURNAMENTS = 1;
    public static final int POSITION_PLAYERS = 2;

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

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() != 0) {
            mViewPager.setCurrentItem(0);
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
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        if (item.isChecked()) {
            return false;
        }

        switch (item.getItemId()) {
            case R.id.actionPlayers:
                mViewPager.setCurrentItem(POSITION_PLAYERS);
                break;

            case R.id.actionRankings:
                mViewPager.setCurrentItem(POSITION_RANKINGS);
                break;

            case R.id.actionTournaments:
                mViewPager.setCurrentItem(POSITION_TOURNAMENTS);
                break;

            default:
                throw new RuntimeException("unknown item: " + item.getTitle());
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miSearch:
                // TODO
                underConstruction();
                return true;

            case R.id.miSettings:
                startActivity(SettingsActivity.getLaunchIntent(this));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnPageChange(R.id.viewPager)
    void onPageChange() {
        updateSelectedBottomNavigationItem();
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        if (!TextUtils.isEmpty(getSubtitle())) {
            menu.findItem(R.id.miSearch).setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onRankingsBundleFetched(@Nullable final RankingsBundle rankingsBundle) {
        if (rankingsBundle == null) {
            setSubtitle("");
        } else {
            setSubtitle(getString(R.string.x_updated_y, mRegionManager.getRegion(this),
                    rankingsBundle.getTime().getSimpleString()));
        }

        supportInvalidateOptionsMenu();
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
        mViewPager.setAdapter(new HomeFragmentAdapter(getSupportFragmentManager()));
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
            case POSITION_PLAYERS:
                mBottomNavigationView.getMenu().findItem(R.id.actionPlayers).setChecked(true);
                break;

            case POSITION_RANKINGS:
                mBottomNavigationView.getMenu().findItem(R.id.actionRankings).setChecked(true);
                break;

            case POSITION_TOURNAMENTS:
                mBottomNavigationView.getMenu().findItem(R.id.actionTournaments).setChecked(true);
                break;
        }
    }

}
