package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.TournamentPagerAdapter;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.SearchQueryHandle;
import com.garpr.android.misc.ShareUtils;
import com.garpr.android.models.AbsTournament;
import com.garpr.android.models.FullTournament;
import com.garpr.android.models.Match;
import com.garpr.android.models.SimpleDate;
import com.garpr.android.networking.ApiCall;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;

import javax.inject.Inject;

import butterknife.BindView;

public class TournamentActivity extends BaseActivity implements ApiListener<FullTournament>,
        MenuItemCompat.OnActionExpandListener, SearchQueryHandle, SearchView.OnQueryTextListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "TournamentActivity";
    private static final String CNAME = TournamentActivity.class.getCanonicalName();
    private static final String EXTRA_TOURNAMENT_DATE = CNAME + ".TournamentDate";
    private static final String EXTRA_TOURNAMENT_ID = CNAME + ".TournamentId";
    private static final String EXTRA_TOURNAMENT_NAME = CNAME + ".TournamentName";

    private FullTournament mFullTournament;
    private SearchView mSearchView;
    private String mTournamentId;
    private TournamentPagerAdapter mAdapter;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ServerApi mServerApi;

    @Inject
    ShareUtils mShareUtils;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.error)
    View mError;

    @BindView(R.id.empty)
    View mEmpty;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;


    public static Intent getLaunchIntent(final Context context,
            @NonNull final AbsTournament tournament) {
        return getLaunchIntent(context, tournament.getId(), tournament.getName(),
                tournament.getDate());
    }

    public static Intent getLaunchIntent(final Context context, @NonNull final Match match) {
        return getLaunchIntent(context, match.getTournament().getId(),
                match.getTournament().getName(), match.getTournament().getDate());
    }

    public static Intent getLaunchIntent(final Context context, @NonNull final String tournamentId,
            @Nullable final String tournamentName, @Nullable final SimpleDate tournamentDate) {
        return getLaunchIntent(context, tournamentId, tournamentName, tournamentDate, null);
    }

    public static Intent getLaunchIntent(final Context context, @NonNull final String tournamentId,
            @Nullable final String tournamentName, @Nullable final SimpleDate tournamentDate,
            @Nullable final String region) {
        final Intent intent = new Intent(context, TournamentActivity.class)
                .putExtra(EXTRA_TOURNAMENT_ID, tournamentId);

        if (!TextUtils.isEmpty(tournamentName)) {
            intent.putExtra(EXTRA_TOURNAMENT_NAME, tournamentName);
        }

        if (tournamentDate != null) {
            intent.putExtra(EXTRA_TOURNAMENT_DATE, tournamentDate);
        }

        if (!TextUtils.isEmpty(region)) {
            intent.putExtra(EXTRA_REGION, region);
        }

        return intent;
    }

    @Override
    public void failure() {
        mFullTournament = null;
        showError();
    }

    private void fetchFullTournament() {
        mFullTournament = null;
        mRefreshLayout.setRefreshing(true);
        mServerApi.getTournament(mRegionManager.getRegion(this), mTournamentId, new ApiCall<>(this));
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Nullable
    @Override
    public CharSequence getSearchQuery() {
        return mSearchView == null ? null : mSearchView.getQuery();
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);
        setContentView(R.layout.activity_tournament);

        final Intent intent = getIntent();
        mTournamentId = intent.getStringExtra(EXTRA_TOURNAMENT_ID);

        prepareMenuAndTitles();

        if (intent.hasExtra(EXTRA_TOURNAMENT_NAME)) {
            setTitle(intent.getStringExtra(EXTRA_TOURNAMENT_NAME));
        }

        fetchFullTournament();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_tournament, menu);

        if (mFullTournament != null) {
            final MenuItem searchMenuItem = menu.findItem(R.id.miSearch);
            searchMenuItem.setVisible(true);

            MenuItemCompat.setOnActionExpandListener(searchMenuItem, this);
            mSearchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
            mSearchView.setQueryHint(getText(R.string.search_));
            mSearchView.setOnQueryTextListener(this);

            menu.findItem(R.id.miShare).setVisible(true);
            menu.findItem(R.id.miViewTournamentPage).setVisible(
                    !TextUtils.isEmpty(mFullTournament.getUrl()));
        }

        return super.onCreateOptionsMenu(menu);
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
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miShare:
                mShareUtils.shareTournament(this, mFullTournament);
                return true;

            case R.id.miViewTournamentPage:
                mShareUtils.openUrl(this, mFullTournament.getUrl());
                return true;
        }

        return super.onOptionsItemSelected(item);
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
    public void onRefresh() {
        fetchFullTournament();
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();

        mRefreshLayout.setOnRefreshListener(this);
        mViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.root_padding));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void prepareMenuAndTitles() {
        final Intent intent = getIntent();

        if (TextUtils.isEmpty(getTitle())) {
            String title = null;

            if (mFullTournament != null) {
                title = mFullTournament.getName();
            } else if (intent.hasExtra(EXTRA_TOURNAMENT_NAME)) {
                title = intent.getStringExtra(EXTRA_TOURNAMENT_NAME);
            }

            if (!TextUtils.isEmpty(title)) {
                setTitle(title);
            }
        }

        if (TextUtils.isEmpty(getSubtitle())) {
            SimpleDate subtitle = null;

            if (mFullTournament != null) {
                subtitle = mFullTournament.getDate();
            } else if (intent.hasExtra(EXTRA_TOURNAMENT_DATE)) {
                subtitle = intent.getParcelableExtra(EXTRA_TOURNAMENT_DATE);
            }

            if (subtitle != null) {
                setSubtitle(subtitle.getLongForm());
            }
        }

        supportInvalidateOptionsMenu();
    }

    private void showEmpty() {
        mError.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        mEmpty.setVisibility(View.VISIBLE);
        prepareMenuAndTitles();
        mRefreshLayout.setRefreshing(false);
    }

    private void showError() {
        mEmpty.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
        prepareMenuAndTitles();
        mRefreshLayout.setRefreshing(false);
    }

    private void showFullTournament() {
        mAdapter = new TournamentPagerAdapter(this, mFullTournament);
        mViewPager.setAdapter(mAdapter);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mViewPager.setVisibility(View.VISIBLE);
        prepareMenuAndTitles();
        mRefreshLayout.setRefreshing(false);
        mRefreshLayout.setEnabled(false);
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    @Override
    public void success(@Nullable final FullTournament fullTournament) {
        mFullTournament = fullTournament;

        if (mFullTournament != null && (mFullTournament.hasMatches() || mFullTournament.hasPlayers())) {
            showFullTournament();
        } else {
            showEmpty();
        }
    }

}
