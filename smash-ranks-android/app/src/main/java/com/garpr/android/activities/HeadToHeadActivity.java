package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.HeadToHeadAdapter;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.ThreadUtils;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.FullTournament;
import com.garpr.android.models.HeadToHead;
import com.garpr.android.models.Match;
import com.garpr.android.networking.ApiCall;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.views.ErrorLinearLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class HeadToHeadActivity extends BaseJavaActivity implements ApiListener<HeadToHead>,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "HeadToHeadActivity";
    private static final String CNAME = HeadToHeadActivity.class.getCanonicalName();
    private static final String EXTRA_PLAYER_ID = CNAME + ".PlayerId";
    private static final String EXTRA_PLAYER_NAME = CNAME + ".PlayerName";
    private static final String EXTRA_OPPONENT_ID = CNAME + ".OpponentId";
    private static final String EXTRA_OPPONENT_NAME = CNAME + ".OpponentName";

    private ArrayList<Object> mList;
    private HeadToHead mHeadToHead;
    private HeadToHeadAdapter mAdapter;
    private Match.Result mResult;
    private String mOpponentId;
    private String mOpponentName;
    private String mPlayerId;
    private String mPlayerName;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ServerApi mServerApi;

    @Inject
    ThreadUtils mThreadUtils;

    @BindView(R.id.error)
    ErrorLinearLayout mError;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.empty)
    View mEmpty;


    public static Intent getLaunchIntent(final Context context, @NonNull final AbsPlayer player,
            @NonNull final AbsPlayer opponent) {
        return getLaunchIntent(context, player.getId(), player.getName(), opponent.getId(),
                opponent.getName());
    }

    public static Intent getLaunchIntent(final Context context, @NonNull final AbsPlayer player,
            @NonNull final Match match) {
        return getLaunchIntent(context, player.getId(), player.getName(),
                match.getOpponent().getId(), match.getOpponent().getName());
    }

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
    public void failure(final int errorCode) {
        mHeadToHead = null;
        mList = null;
        mResult = null;
        showError(errorCode);
    }

    private void fetchHeadToHead() {
        mRefreshLayout.setRefreshing(true);
        mServerApi.getHeadToHead(mRegionManager.getRegion(this), mPlayerId, mOpponentId,
                new ApiCall<>(this));
    }

    private void filter(@Nullable final Match.Result result) {
        mResult = result;

        if (mList == null || mList.isEmpty()) {
            return;
        }

        mThreadUtils.run(new ThreadUtils.Task() {
            private List<Object> mList;

            @Override
            public void onBackground() {
                if (!isAlive() || mResult != result) {
                    return;
                }

                mList = ListUtils.filterPlayerMatchesList(result, HeadToHeadActivity.this.mList);
            }

            @Override
            public void onUi() {
                if (!isAlive() || mResult != result) {
                    return;
                }

                mAdapter.set(mList);
                supportInvalidateOptionsMenu();
            }
        });
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

        if (intent.hasExtra(EXTRA_OPPONENT_NAME) && intent.hasExtra(EXTRA_PLAYER_NAME)) {
            mOpponentName = intent.getStringExtra(EXTRA_OPPONENT_NAME);
            mPlayerName = intent.getStringExtra(EXTRA_PLAYER_NAME);
            setSubtitle();
        }

        fetchHeadToHead();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_head_to_head, menu);

        if (mHeadToHead != null) {
            menu.findItem(R.id.miFilter).setVisible(true);
            menu.findItem(R.id.miFilterAll).setVisible(mResult != null);
            menu.findItem(R.id.miFilterLosses).setVisible(mResult != Match.Result.LOSE);
            menu.findItem(R.id.miFilterWins).setVisible(mResult != Match.Result.WIN);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miFilterAll:
                filter(null);
                return true;

            case R.id.miFilterLosses:
                filter(Match.Result.LOSE);
                return true;

            case R.id.miFilterWins:
                filter(Match.Result.WIN);
                return true;

            case R.id.miViewOpponent:
                startActivity(PlayerActivity.getLaunchIntent(this, mOpponentId, mOpponentName,
                        mRegionManager.getRegion(this)));
                return true;

            case R.id.miViewPlayer:
                startActivity(PlayerActivity.getLaunchIntent(this, mPlayerId, mPlayerName,
                        mRegionManager.getRegion(this)));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.findItem(R.id.miFilter).setVisible(mHeadToHead != null);

        if (!TextUtils.isEmpty(mOpponentName) && !TextUtils.isEmpty(mPlayerName)) {
            final MenuItem viewOpponent = menu.findItem(R.id.miViewOpponent);
            viewOpponent.setTitle(getString(R.string.view_x, mOpponentName));
            viewOpponent.setVisible(true);

            final MenuItem viewPlayer = menu.findItem(R.id.miViewPlayer);
            viewPlayer.setTitle(getString(R.string.view_x, mPlayerName));
            viewPlayer.setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onRefresh() {
        fetchHeadToHead();
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();

        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new HeadToHeadAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void prepareMenuAndSubtitle() {
        if (mHeadToHead != null) {
            if (TextUtils.isEmpty(mOpponentName)) {
                mOpponentName = mHeadToHead.getOpponent().getName();
            }

            if (TextUtils.isEmpty(mPlayerName)) {
                mPlayerName = mHeadToHead.getPlayer().getName();
            }
        }

        setSubtitle();
        supportInvalidateOptionsMenu();
    }

    private void showData() {
        mList = ListUtils.createHeadToHeadList(this, mHeadToHead);
        mAdapter.set(mList);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        prepareMenuAndSubtitle();
        mRefreshLayout.setRefreshing(false);
    }

    private void showEmpty() {
        mAdapter.clear();
        mRecyclerView.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mEmpty.setVisibility(View.VISIBLE);
        prepareMenuAndSubtitle();
        mRefreshLayout.setRefreshing(false);
    }

    private void showError(final int errorCode) {
        mAdapter.clear();
        mRecyclerView.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE, errorCode);
        prepareMenuAndSubtitle();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    private void setSubtitle() {
        if (TextUtils.isEmpty(getSubtitle()) && !TextUtils.isEmpty(mPlayerName) &&
                !TextUtils.isEmpty(mOpponentName)) {
            setSubtitle(getString(R.string.x_vs_y, mPlayerName, mOpponentName));
        }
    }

    @Override
    public void success(@Nullable final HeadToHead headToHead) {
        mHeadToHead = headToHead;

        if (mHeadToHead == null) {
            showEmpty();
        } else {
            showData();
        }
    }

}
