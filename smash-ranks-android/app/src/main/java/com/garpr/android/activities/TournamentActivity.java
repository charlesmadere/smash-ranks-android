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
import android.view.View;

import com.garpr.android.R;
import com.garpr.android.models.AbsTournament;
import com.garpr.android.views.RefreshLayout;

import butterknife.BindView;

public class TournamentActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "TournamentActivity";
    private static final String CNAME = TournamentActivity.class.getCanonicalName();
    private static final String EXTRA_TOURNAMENT_ID = CNAME + ".TournamentId";
    private static final String EXTRA_TOURNAMENT_NAME = CNAME + ".TournamentName";

    private String mTournamentId;

    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.error)
    View mError;

    @BindView(R.id.empty)
    View mEmpty;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;


    public static Intent getLaunchIntent(final Context context, @NonNull final AbsTournament tournament) {
        return getLaunchIntent(context, tournament.getId(), tournament.getName());
    }

    public static Intent getLaunchIntent(final Context context, @NonNull final String tournamentId,
            @Nullable final String tournamentName) {
        final Intent intent = new Intent(context, TournamentActivity.class)
                .putExtra(EXTRA_TOURNAMENT_ID, tournamentId);

        if (!TextUtils.isEmpty(tournamentName)) {
            intent.putExtra(EXTRA_TOURNAMENT_NAME, tournamentName);
        }

        return intent;
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        final Intent intent = getIntent();
        mTournamentId = intent.getStringExtra(EXTRA_TOURNAMENT_ID);

        if (intent.hasExtra(EXTRA_TOURNAMENT_NAME)) {
            setTitle(intent.getStringExtra(EXTRA_TOURNAMENT_NAME));
        }

        // TODO
    }

    @Override
    public void onRefresh() {
        // TODO
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();

        mRefreshLayout.setOnRefreshListener(this);
        // TODO
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

}
