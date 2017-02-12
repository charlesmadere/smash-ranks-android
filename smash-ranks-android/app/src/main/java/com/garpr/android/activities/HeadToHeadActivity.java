package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.HeadToHeadAdapter;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.models.FullTournament;
import com.garpr.android.models.HeadToHead;
import com.garpr.android.networking.ApiCall;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.views.RefreshLayout;

import javax.inject.Inject;

import butterknife.BindView;

public class HeadToHeadActivity extends BaseActivity implements ApiListener<HeadToHead>,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "HeadToHeadActivity";
    private static final String CNAME = HeadToHeadActivity.class.getCanonicalName();
    private static final String EXTRA_PLAYER_ID = CNAME + ".PlayerId";
    private static final String EXTRA_PLAYER_NAME = CNAME + ".PlayerName";
    private static final String EXTRA_OPPONENT_ID = CNAME + ".OpponentId";
    private static final String EXTRA_OPPONENT_NAME = CNAME + ".OpponentName";

    private HeadToHead mHeadToHead;
    private HeadToHeadAdapter mAdapter;
    private String mOpponentId;
    private String mPlayerId;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ServerApi mServerApi;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    @BindView(R.id.empty)
    View mEmpty;

    @BindView(R.id.error)
    View mError;


    public static Intent getLaunchIntent(final Context context,
            @NonNull final FullTournament.Match match) {
        return getLaunchIntent(context, match.getWinnerId(), match.getWinnerName(),
                match.getLoserId(), match.getLoserName());
    }

    public static Intent getLaunchIntent(final Context context, @NonNull final String playerId,
            @Nullable final String playerName, @NonNull final String opponentId,
            @Nullable final String opponentName) {
        final Intent intent = new Intent(context, HeadToHeadActivity.class)
                .putExtra(EXTRA_PLAYER_ID, playerId)
                .putExtra(EXTRA_OPPONENT_ID, opponentId);

        if (!TextUtils.isEmpty(playerName) && !TextUtils.isEmpty(opponentName)) {
            intent.putExtra(EXTRA_PLAYER_NAME, playerName);
            intent.putExtra(EXTRA_OPPONENT_NAME, opponentName);
        }

        return intent;
    }

    @Override
    public void failure() {
        mHeadToHead = null;
        showError();
    }

    private void fetchHeadToHead() {
        mHeadToHead = null;
        mRefreshLayout.setRefreshing(true);
        mServerApi.getHeadToHead(mRegionManager.getRegion(this), mPlayerId, mOpponentId,
                new ApiCall<>(this));
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);
        setContentView(R.layout.activity_head_to_head);

        final Intent intent = getIntent();
        mOpponentId = intent.getStringExtra(EXTRA_OPPONENT_ID);
        mPlayerId = intent.getStringExtra(EXTRA_PLAYER_ID);

        if (intent.hasExtra(EXTRA_PLAYER_NAME) && intent.hasExtra(EXTRA_OPPONENT_NAME)) {
            setSubtitle(getString(R.string.x_vs_y, intent.getStringExtra(EXTRA_PLAYER_NAME),
                    intent.getStringExtra(EXTRA_OPPONENT_NAME)));
        }

        fetchHeadToHead();
    }

    @Override
    public void onRefresh() {
        fetchHeadToHead();
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();

        mRefreshLayout.setOnRefreshListener(this);
        mAdapter = new HeadToHeadAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showData() {
        mAdapter.set(ListUtils.createHeadToHeadList(mHeadToHead));
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
    }

    private void showEmpty() {
        mAdapter.clear();
        mRecyclerView.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mEmpty.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
    }

    private void showError() {
        mAdapter.clear();
        mRecyclerView.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    @Override
    public void success(@Nullable final HeadToHead headToHead) {
        mHeadToHead = headToHead;

        if (mHeadToHead != null && mHeadToHead.hasMatches()) {
            showData();
        } else {
            showEmpty();
        }
    }

}
