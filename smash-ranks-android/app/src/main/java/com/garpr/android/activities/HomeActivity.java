package com.garpr.android.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.HomePagerAdapter;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.NotificationManager;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.SearchQueryHandle;
import com.garpr.android.misc.ShareUtils;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.models.Region;
import com.garpr.android.sync.RankingsPollingSyncManager;
import com.garpr.android.views.RankingsLayout;
import com.garpr.android.views.SearchLayout;
import com.garpr.android.views.toolbars.HomeToolbar;

import java.text.NumberFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnPageChange;

public class HomeActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener, HomeToolbar.Listeners,
        RankingsLayout.Listener, RegionManager.OnRegionChangeListener, SearchQueryHandle {

    private static final String TAG = "HomeActivity";
    private static final String CNAME = HomeActivity.class.getCanonicalName();
    private static final String EXTRA_INITIAL_POSITION = CNAME + ".InitialPosition";
    private static final String KEY_CURRENT_POSITION = "CurrentPosition";

    public static final int POSITION_RANKINGS = 0;
    public static final int POSITION_TOURNAMENTS = 1;
    public static final int POSITION_FAVORITE_PLAYERS = 2;

    private HomePagerAdapter mAdapter;

    @Inject
    IdentityManager mIdentityManager;

    @Inject
    NotificationManager mNotificationManager;

    @Inject
    RankingsPollingSyncManager mRankingsPollingSyncManager;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ShareUtils mShareUtils;

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView mBottomNavigationView;

    @BindView(R.id.homeToolbar)
    HomeToolbar mHomeToolbar;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;


    public static Intent getLaunchIntent(final Context context) {
        return getLaunchIntent(context, null);
    }

    public static Intent getLaunchIntent(final Context context,
            @Nullable final Integer initialPosition) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent = IntentCompat.makeRestartActivityTask(intent.getComponent());

        if (initialPosition != null) {
            intent.putExtra(EXTRA_INITIAL_POSITION, initialPosition.intValue());
        }

        return intent;
    }

    @Override
    public String getActivityName() {
        return TAG;
    }

    @Nullable
    @Override
    public CharSequence getSearchQuery() {
        return mHomeToolbar.getSearchQuery();
    }

    @Override
    public void onActivityRequirementsButtonClick() {
        final Region region = mRegionManager.getRegion(this);
        final Integer rankingNumTourneysAttended = region.getRankingNumTourneysAttended();
        final Integer rankingActivityDayLimit = region.getRankingActivityDayLimit();

        if (rankingNumTourneysAttended == null || rankingActivityDayLimit == null) {
            throw new RuntimeException("Region (" + region + ") is missing necessary data");
        }

        final NumberFormat numberFormat = NumberFormat.getInstance();
        final String tournaments = getResources().getQuantityString(R.plurals.x_tournaments,
                rankingNumTourneysAttended, numberFormat.format(rankingNumTourneysAttended));
        final String days = getResources().getQuantityString(R.plurals.x_days,
                rankingActivityDayLimit, numberFormat.format(rankingActivityDayLimit));

        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.x_within_the_last_y, tournaments, days))
                .setTitle(getString(R.string.x_activity_requirements, region.getDisplayName()))
                .show();
    }

    @Override
    public void onBackPressed() {
        if (mHomeToolbar != null && mHomeToolbar.isSearchLayoutExpanded()) {
            mHomeToolbar.closeSearchLayout();
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

        mRegionManager.addListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRegionManager.removeListener(this);
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
            case R.id.miSettings:
                startActivity(SettingsActivity.getLaunchIntent(this));
                return true;

            case R.id.miShare:
                share();
                return true;

            case R.id.miViewAllPlayers:
                startActivity(PlayersActivity.getLaunchIntent(this));
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
    public void onRankingsBundleFetched(final RankingsLayout layout) {
        final Region region = mRegionManager.getRegion(this);
        setTitle(region.getEndpoint().getName());

        final RankingsBundle rankingsBundle = layout.getRankingsBundle();

        if (rankingsBundle == null) {
            setSubtitle(region.getDisplayName());
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

//        if (mSearchMenuItem != null && MenuItemCompat.isActionViewExpanded(mSearchMenuItem)) {
//            MenuItemCompat.collapseActionView(mSearchMenuItem);
//        }

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
    public void onSearchFieldTextChanged(final SearchLayout searchLayout) {
        mAdapter.search(searchLayout.getText());
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

    private void share() {
        CharSequence[] items = {
                getText(R.string.rankings),
                getText(R.string.tournaments)
        };

        new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        switch (which) {
                            case 0:
                                mShareUtils.shareRankings(HomeActivity.this);
                                break;

                            case 1:
                                mShareUtils.shareTournaments(HomeActivity.this);
                                break;

                            default:
                                throw new RuntimeException("illegal which: " + which);
                        }
                    }
                })
                .setTitle(R.string.share)
                .show();
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
