package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.PlayerAdapter;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.FullPlayer;
import com.garpr.android.models.MatchesBundle;
import com.garpr.android.models.Ranking;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.views.RefreshLayout;

import javax.inject.Inject;

import butterknife.BindView;

public class PlayerActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "PlayerActivity";
    private static final String CNAME = PlayerActivity.class.getCanonicalName();
    private static final String EXTRA_PLAYER_ID = CNAME + ".PlayerId";
    private static final String EXTRA_PLAYER_NAME = CNAME + ".PlayerName";
    private static final String KEY_FULL_PLAYER = "FullPlayer";
    private static final String KEY_MATCHES_BUNDLE = "MatchesBundle";

    private FullPlayer mFullPlayer;
    private MatchesBundle mMatchesBundle;
    private PlayerAdapter mAdapter;
    private String mPlayerId;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ServerApi mServerApi;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    @BindView(R.id.error)
    View mError;


    public static Intent getLaunchIntent(final Context context, @NonNull final AbsPlayer player) {
        return getLaunchIntent(context, player.getId(), player.getName());
    }

    public static Intent getLaunchIntent(final Context context, @NonNull final Ranking ranking) {
        return getLaunchIntent(context, ranking.getId(), ranking.getName());
    }

    public static Intent getLaunchIntent(final Context context, @NonNull final String playerId,
            @Nullable final String playerName) {
        final Intent intent = new Intent(context, PlayerActivity.class)
                .putExtra(EXTRA_PLAYER_ID, playerId);

        if (!TextUtils.isEmpty(playerName)) {
            intent.putExtra(EXTRA_PLAYER_NAME, playerName);
        }

        return intent;
    }

    private void failure() {
        mFullPlayer = null;
        mMatchesBundle = null;
        showError();
    }

    private void fetchData() {
        mFullPlayer = null;
        mMatchesBundle = null;
        mRefreshLayout.setRefreshing(true);

        final String region = mRegionManager.getCurrentRegion(this);
        mServerApi.getMatches(region, mPlayerId, mMatchesBundleListener);
        mServerApi.getPlayer(region, mPlayerId, mFullPlayerListener);
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);
        setContentView(R.layout.activity_player);

        final Intent intent = getIntent();
        mPlayerId = intent.getStringExtra(EXTRA_PLAYER_ID);

        if (intent.hasExtra(EXTRA_PLAYER_NAME)) {
            setTitle(intent.getStringExtra(EXTRA_PLAYER_NAME));
        }

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            mFullPlayer = savedInstanceState.getParcelable(KEY_FULL_PLAYER);
            mMatchesBundle = savedInstanceState.getParcelable(KEY_MATCHES_BUNDLE);
        }

        if (mFullPlayer == null || mMatchesBundle == null) {
            fetchData();
        } else {
            showData();
        }
    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mFullPlayer != null && mMatchesBundle != null) {
            outState.putParcelable(KEY_FULL_PLAYER, mFullPlayer);
            outState.putParcelable(KEY_MATCHES_BUNDLE, mMatchesBundle);
        }
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();

        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        mAdapter = new PlayerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showData() {
        mAdapter.set(mFullPlayer, mMatchesBundle);
        mError.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
    }

    private void showError() {
        mAdapter.clear();
        mRecyclerView.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    private void success() {
        if (mFullPlayer != null && mMatchesBundle != null) {
            showData();
        }
    }

    private final ApiListener<FullPlayer> mFullPlayerListener = new ApiListener<FullPlayer>() {
        @Override
        public void failure() {
            PlayerActivity.this.failure();
        }

        @Override
        public boolean isAlive() {
            return PlayerActivity.this.isAlive();
        }

        @Override
        public void success(@Nullable final FullPlayer fullPlayer) {
            mFullPlayer = fullPlayer;
            PlayerActivity.this.success();
        }
    };

    private final ApiListener<MatchesBundle> mMatchesBundleListener = new ApiListener<MatchesBundle>() {
        @Override
        public void failure() {
            PlayerActivity.this.failure();
        }

        @Override
        public boolean isAlive() {
            return PlayerActivity.this.isAlive();
        }

        @Override
        public void success(@Nullable final MatchesBundle matchesBundle) {
            mMatchesBundle = matchesBundle;
            PlayerActivity.this.success();
        }
    };

}
