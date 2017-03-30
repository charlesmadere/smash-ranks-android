package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.PlayerAdapter;
import com.garpr.android.misc.FavoritePlayersManager;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.SearchQueryHandle;
import com.garpr.android.misc.ShareUtils;
import com.garpr.android.misc.ThreadUtils;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.FullPlayer;
import com.garpr.android.models.Match;
import com.garpr.android.models.MatchesBundle;
import com.garpr.android.models.Ranking;
import com.garpr.android.models.Region;
import com.garpr.android.networking.ApiCall;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.views.MatchItemView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PlayerActivity extends BaseActivity implements
        FavoritePlayersManager.OnFavoritePlayersChangeListener,
        IdentityManager.OnIdentityChangeListener, MatchItemView.OnClickListener,
        MenuItemCompat.OnActionExpandListener, SearchQueryHandle, SearchView.OnQueryTextListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "PlayerActivity";
    private static final String CNAME = PlayerActivity.class.getCanonicalName();
    private static final String EXTRA_PLAYER_ID = CNAME + ".PlayerId";
    private static final String EXTRA_PLAYER_NAME = CNAME + ".PlayerName";

    private ArrayList<Object> mList;
    private DataListener mDataListener;
    private FullPlayer mFullPlayer;
    private MatchesBundle mMatchesBundle;
    private Match.Result mResult;
    private PlayerAdapter mAdapter;
    private SearchView mSearchView;
    private String mPlayerId;

    @Inject
    FavoritePlayersManager mFavoritePlayersManager;

    @Inject
    IdentityManager mIdentityManager;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ServerApi mServerApi;

    @Inject
    ShareUtils mShareUtils;

    @Inject
    ThreadUtils mThreadUtils;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

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
        return getLaunchIntent(context, playerId, playerName, null);
    }

    public static Intent getLaunchIntent(final Context context, @NonNull final String playerId,
            @Nullable final String playerName, @Nullable final Region region) {
        final Intent intent = new Intent(context, PlayerActivity.class)
                .putExtra(EXTRA_PLAYER_ID, playerId);

        if (!TextUtils.isEmpty(playerName)) {
            intent.putExtra(EXTRA_PLAYER_NAME, playerName);
        }

        if (region != null) {
            intent.putExtra(EXTRA_REGION, region);
        }

        return intent;
    }

    private void failure() {
        mFullPlayer = null;
        mList = null;
        mMatchesBundle = null;
        mResult = null;
        showError();
    }

    private void fetchData() {
        mFullPlayer = null;
        mList = null;
        mMatchesBundle = null;
        mResult = null;
        mRefreshLayout.setRefreshing(true);
        mDataListener = new DataListener();
        mDataListener.fetch();
    }

    private void filter(@Nullable final Match.Result result) {
        mResult = result;

        mThreadUtils.run(new ThreadUtils.Task() {
            private List<Object> mList;

            @Override
            public void onBackground() {
                mList = ListUtils.filterPlayerMatchesList(result, PlayerActivity.this.mList);
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

    @Nullable
    @Override
    public CharSequence getSearchQuery() {
        return mSearchView == null ? null : mSearchView.getQuery();
    }

    @Override
    public void onClick(final MatchItemView v) {
        final Match match = v.getContent();
        startActivity(HeadToHeadActivity.getLaunchIntent(this, mFullPlayer, match));
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

        mFavoritePlayersManager.addListener(this);
        mIdentityManager.addListener(this);
        fetchData();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_player, menu);

        if (mFullPlayer != null) {
            if (mMatchesBundle != null && mMatchesBundle.hasMatches()) {
                final MenuItem searchMenuItem = menu.findItem(R.id.miSearch);
                searchMenuItem.setVisible(true);

                MenuItemCompat.setOnActionExpandListener(searchMenuItem, this);
                mSearchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
                mSearchView.setQueryHint(getText(R.string.search_opponents_));
                mSearchView.setOnQueryTextListener(this);

                menu.findItem(R.id.miFilter).setVisible(true);
                menu.findItem(R.id.miFilterAll).setVisible(mResult != null);
                menu.findItem(R.id.miFilterLosses).setVisible(mResult != Match.Result.LOSE);
                menu.findItem(R.id.miFilterWins).setVisible(mResult != Match.Result.WIN);
            }

            menu.findItem(R.id.miShare).setVisible(true);

            if (mFavoritePlayersManager.containsPlayer(mPlayerId)) {
                menu.findItem(R.id.miRemoveFromFavorites).setVisible(true);
            } else {
                menu.findItem(R.id.miAddToFavorites).setVisible(true);
            }

            if (mFullPlayer.hasAliases()) {
                menu.findItem(R.id.miAliases).setVisible(true);
            }

            if (mIdentityManager.hasIdentity()) {
                final MenuItem menuItem = menu.findItem(R.id.miViewYourselfVsThisOpponent);
                menuItem.setTitle(getString(R.string.view_yourself_vs_x, mFullPlayer.getName()));
                menuItem.setVisible(true);
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataListener = null;
        mFavoritePlayersManager.removeListener(this);
        mIdentityManager.removeListener(this);
    }

    @Override
    public void onFavoritePlayersChanged(final FavoritePlayersManager manager) {
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onIdentityChange(final IdentityManager identityManager) {
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onMenuItemActionCollapse(final MenuItem item) {
        search(null);
        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(final MenuItem item) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miAddToFavorites:
                mFavoritePlayersManager.addPlayer(mFullPlayer);
                return true;

            case R.id.miAliases:
                showAliases();
                return true;

            case R.id.miFilterAll:
                filter(null);
                return true;

            case R.id.miFilterLosses:
                filter(Match.Result.LOSE);
                return true;

            case R.id.miFilterWins:
                filter(Match.Result.WIN);
                return true;

            case R.id.miRemoveFromFavorites:
                mFavoritePlayersManager.removePlayer(mPlayerId);
                return true;

            case R.id.miShare:
                mShareUtils.sharePlayer(this, mFullPlayer);
                return true;

            case R.id.miViewYourselfVsThisOpponent:
                // noinspection ConstantConditions
                startActivity(HeadToHeadActivity.getLaunchIntent(this,
                        mIdentityManager.getIdentity(), mFullPlayer));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        search(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        search(query);
        return false;
    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();

        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new PlayerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void search(@Nullable final String query) {
        mThreadUtils.run(new ThreadUtils.Task() {
            private List<Object> mList;

            @Override
            public void onBackground() {
                if (!isAlive()) {
                    return;
                }

                mList = ListUtils.searchPlayerMatchesList(query, PlayerActivity.this.mList);
            }

            @Override
            public void onUi() {
                if (!isAlive() || !TextUtils.equals(query, getSearchQuery())) {
                    return;
                }

                mAdapter.set(mList);
            }
        });
    }

    private void showAliases() {
        final ArrayList<String> aliases = mFullPlayer.getAliases();
        // noinspection ConstantConditions
        final CharSequence[] items = new CharSequence[aliases.size()];
        aliases.toArray(items);

        new AlertDialog.Builder(this)
                .setItems(items, null)
                .setTitle(R.string.aliases)
                .show();
    }

    private void showData() {
        mList = ListUtils.createPlayerMatchesList(this, mRegionManager, mFullPlayer, mMatchesBundle);

        mError.setVisibility(View.GONE);

        if (mList == null || mList.isEmpty()) {
            mAdapter.clear();
            mRecyclerView.setVisibility(View.GONE);
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mAdapter.set(mList);
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
        private final Region mRegion;

        private DataListener() {
            mRegion = mRegionManager.getRegion(PlayerActivity.this);
        }

        private void fetch() {
            mServerApi.getMatches(mRegion, mPlayerId, new ApiCall<>(mMatchesBundleListener));
            mServerApi.getPlayer(mRegion, mPlayerId, new ApiCall<>(mFullPlayerListener));
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
