package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
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
import com.garpr.android.models.Region;
import com.garpr.android.models.SimpleDate;
import com.garpr.android.networking.ApiCall;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.views.ErrorLinearLayout;
import com.garpr.android.views.toolbars.SearchToolbar;
import com.garpr.android.views.toolbars.TournamentToolbar;

import javax.inject.Inject;

import butterknife.BindView;

public class TournamentActivity extends BaseActivity implements ApiListener<FullTournament>,
        SearchQueryHandle, SearchToolbar.Listener, SwipeRefreshLayout.OnRefreshListener,
        TournamentToolbar.DataProvider {

    private static final String TAG = "TournamentActivity";
    private static final String CNAME = TournamentActivity.class.getCanonicalName();
    private static final String EXTRA_TOURNAMENT_DATE = CNAME + ".TournamentDate";
    private static final String EXTRA_TOURNAMENT_ID = CNAME + ".TournamentId";
    private static final String EXTRA_TOURNAMENT_NAME = CNAME + ".TournamentName";

    private FullTournament mFullTournament;
    private String mTournamentId;
    private TournamentPagerAdapter mAdapter;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ServerApi mServerApi;

    @Inject
    ShareUtils mShareUtils;

    @BindView(R.id.error)
    ErrorLinearLayout mError;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.toolbar)
    TournamentToolbar mTournamentToolbar;

    @BindView(R.id.empty)
    View mEmpty;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;


    public static Intent getLaunchIntent(final Context context,
            @NonNull final AbsTournament tournament, @Nullable final Region region) {
        return getLaunchIntent(context, tournament.getId(), tournament.getName(),
                tournament.getDate(), region);
    }

    public static Intent getLaunchIntent(final Context context, @NonNull final Match match,
            @Nullable final Region region) {
        return getLaunchIntent(context, match.getTournament().getId(),
                match.getTournament().getName(), match.getTournament().getDate(), region);
    }

    public static Intent getLaunchIntent(final Context context, @NonNull final String tournamentId,
            @Nullable final String tournamentName, @Nullable final SimpleDate tournamentDate,
            @Nullable final Region region) {
        final Intent intent = new Intent(context, TournamentActivity.class)
                .putExtra(EXTRA_TOURNAMENT_ID, tournamentId);

        if (!TextUtils.isEmpty(tournamentName)) {
            intent.putExtra(EXTRA_TOURNAMENT_NAME, tournamentName);
        }

        if (tournamentDate != null) {
            intent.putExtra(EXTRA_TOURNAMENT_DATE, tournamentDate);
        }

        if (region != null) {
            intent.putExtra(EXTRA_REGION, region);
        }

        return intent;
    }

    @Override
    public void failure(final int errorCode) {
        mFullTournament = null;
        showError(errorCode);
    }

    private void fetchFullTournament() {
        mRefreshLayout.setRefreshing(true);
        mServerApi.getTournament(mRegionManager.getRegion(this), mTournamentId, new ApiCall<>(this));
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Nullable
    @Override
    public FullTournament getFullTournament() {
        return mFullTournament;
    }

    @Nullable
    @Override
    public CharSequence getSearchQuery() {
        return mTournamentToolbar.getSearchQuery();
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);
        setContentView(R.layout.activity_tournament);

        final Intent intent = getIntent();
        mTournamentId = intent.getStringExtra(EXTRA_TOURNAMENT_ID);

        prepareMenuAndTitles();
        fetchFullTournament();
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

    private void showError(final int errorCode) {
        mEmpty.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE, errorCode);
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
    public boolean showSearchMenuItem() {
        return mFullTournament != null;
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
