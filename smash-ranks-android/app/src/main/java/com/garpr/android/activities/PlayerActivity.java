package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.PlayerAdapter;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.ShareUtils;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.FullPlayer;
import com.garpr.android.models.MatchesBundle;
import com.garpr.android.models.Ranking;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.views.RefreshLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

public class PlayerActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "PlayerActivity";
    private static final String CNAME = PlayerActivity.class.getCanonicalName();
    private static final String EXTRA_PLAYER_ID = CNAME + ".PlayerId";
    private static final String EXTRA_PLAYER_NAME = CNAME + ".PlayerName";

    private FullPlayer mFullPlayer;
    private MatchesBundle mMatchesBundle;
    private PlayerAdapter mAdapter;
    private String mPlayerId;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ServerApi mServerApi;

    @Inject
    ShareUtils mShareUtils;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    @BindView(R.id.empty)
    View mEmpty;

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
        new DataListener();
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

        fetchData();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_player, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miAliases:
                showAliases();
                return true;

            case R.id.miShare:
                mShareUtils.sharePlayer(this, mFullPlayer);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        if (mFullPlayer != null) {
            menu.findItem(R.id.miShare).setVisible(true);

            if (mFullPlayer.hasAliases()) {
                menu.findItem(R.id.miAliases).setVisible(true);
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();

        mRefreshLayout.setOnRefreshListener(this);
        mAdapter = new PlayerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showAliases() {
        final ArrayList<String> aliases = mFullPlayer.getAliases();
        // noinspection ConstantConditions
        final CharSequence[] items = new CharSequence[aliases.size()];
        aliases.toArray(items);

        new AlertDialog.Builder(this)
                .setItems(items, null)
                .show();
    }

    private void showData() {
        final ArrayList<Object> list = ListUtils.createPlayerList(this, mRegionManager,
                mFullPlayer, mMatchesBundle);

        mError.setVisibility(View.GONE);

        if (list == null || list.isEmpty()) {
            mAdapter.clear();
            mRecyclerView.setVisibility(View.GONE);
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mAdapter.set(list);
            mEmpty.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        mRefreshLayout.setRefreshing(false);

        if (TextUtils.isEmpty(getTitle())) {
            setTitle(mFullPlayer.getName());
        }

        supportInvalidateOptionsMenu();
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

    private void success(@NonNull final FullPlayer fullPlayer,
            @Nullable final MatchesBundle matchesBundle) {
        mFullPlayer = fullPlayer;
        mMatchesBundle = matchesBundle;
        showData();
    }


    private final class DataListener {
        private boolean mFullPlayerFound;
        private boolean mMatchesBundleFound;
        private FullPlayer mFullPlayer;
        private MatchesBundle mMatchesBundle;

        private DataListener() {
            final String region = mRegionManager.getRegion(PlayerActivity.this);
            mServerApi.getMatches(region, mPlayerId, mMatchesBundleListener);
            mServerApi.getPlayer(region, mPlayerId, mFullPlayerListener);
        }

        private void proceed() {
            if (mFullPlayerFound && mMatchesBundleFound) {
                // intentionally not checking to see if mMatchesBundle is null, PlayerActivity is
                // designed to handle a null MatchesBundle
                if (mFullPlayer == null) {
                    failure();
                } else {
                    success(mFullPlayer, mMatchesBundle);
                }
            }
        }

        private final ApiListener<FullPlayer> mFullPlayerListener = new ApiListener<FullPlayer>() {
            @Override
            public void failure() {
                mFullPlayerFound = true;
                mFullPlayer = null;
                proceed();
            }

            @Override
            public boolean isAlive() {
                return PlayerActivity.this.isAlive();
            }

            @Override
            public void success(@Nullable final FullPlayer fullPlayer) {
                mFullPlayerFound = true;
                mFullPlayer = fullPlayer;
                proceed();
            }
        };

        private final ApiListener<MatchesBundle> mMatchesBundleListener = new ApiListener<MatchesBundle>() {
            @Override
            public void failure() {
                mMatchesBundleFound = true;
                mMatchesBundle = null;
                proceed();
            }

            @Override
            public boolean isAlive() {
                return PlayerActivity.this.isAlive();
            }

            @Override
            public void success(@Nullable final MatchesBundle matchesBundle) {
                mMatchesBundleFound = true;
                mMatchesBundle = matchesBundle;
                proceed();
            }
        };
    }

}
