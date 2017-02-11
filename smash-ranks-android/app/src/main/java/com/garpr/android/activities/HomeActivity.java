package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.ViewPager;
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
        Intent intent = new Intent(context, HomeActivity.class);
        return IntentCompat.makeRestartActivityTask(intent.getComponent());
    }

    @Override
    public String getActivityName() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);
        setContentView(R.layout.activity_home);

        mRankingsPollingSyncManager.enableOrDisable();
        mNotificationManager.cancelAll();
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
            case R.id.actionRankings:
                mViewPager.setCurrentItem(HomeFragmentAdapter.POSITION_RANKINGS);
                break;

            case R.id.actionTournaments:
                mViewPager.setCurrentItem(HomeFragmentAdapter.POSITION_TOURNAMENTS);
                break;

            case R.id.actionPlayers:
                mViewPager.setCurrentItem(HomeFragmentAdapter.POSITION_PLAYERS);
                break;

            default:
                throw new RuntimeException("unknown item: " + item.getTitle());
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
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
    public void onRankingsBundleFetched(@Nullable final RankingsBundle rankingsBundle) {
        if (rankingsBundle == null) {
            setSubtitle("");
        } else {
            setSubtitle(getString(R.string.x_updated_y, mRegionManager.getRegion(this),
                    rankingsBundle.getTime().getDateString()));
        }
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();

        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.root_padding));
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new HomeFragmentAdapter(getSupportFragmentManager()));
    }

    private void updateSelectedBottomNavigationItem() {
        switch (mViewPager.getCurrentItem()) {
            case HomeFragmentAdapter.POSITION_RANKINGS:
                mBottomNavigationView.getMenu().findItem(R.id.actionRankings).setChecked(true);
                break;

            case HomeFragmentAdapter.POSITION_PLAYERS:
                mBottomNavigationView.getMenu().findItem(R.id.actionPlayers).setChecked(true);
                break;

            case HomeFragmentAdapter.POSITION_TOURNAMENTS:
                mBottomNavigationView.getMenu().findItem(R.id.actionTournaments).setChecked(true);
                break;
        }
    }

}
