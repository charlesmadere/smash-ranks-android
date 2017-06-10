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
import android.view.MenuItem;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.PlayerAdapter;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.FavoritePlayersManager;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.SearchQueryHandle;
import com.garpr.android.misc.Searchable;
import com.garpr.android.misc.ShareUtils;
import com.garpr.android.misc.ThreadUtils;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.AbsTournament;
import com.garpr.android.models.FavoritePlayer;
import com.garpr.android.models.FullPlayer;
import com.garpr.android.models.Match;
import com.garpr.android.models.MatchesBundle;
import com.garpr.android.models.Ranking;
import com.garpr.android.models.Region;
import com.garpr.android.networking.ApiCall;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.views.ErrorLinearLayout;
import com.garpr.android.views.MatchItemView;
import com.garpr.android.views.TournamentDividerView;
import com.garpr.android.views.toolbars.PlayerToolbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PlayerActivity extends BaseActivity implements
        MatchItemView.OnClickListener, PlayerToolbar.DataProvider, Searchable, SearchQueryHandle,
        SwipeRefreshLayout.OnRefreshListener, TournamentDividerView.OnClickListener {

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

    @BindView(R.id.error)
    ErrorLinearLayout mError;

    @BindView(R.id.toolbar)
    PlayerToolbar mPlayerToolbar;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.empty)
    View mEmpty;


    public static Intent getLaunchIntent(final Context context, @NonNull final AbsPlayer player,
            @Nullable Region region) {
        if (player instanceof FavoritePlayer) {
            region = ((FavoritePlayer) player).getRegion();
        }

        return getLaunchIntent(context, player.getId(), player.getName(), region);
    }

    public static Intent getLaunchIntent(final Context context, @NonNull final Ranking ranking,
            @Nullable final Region region) {
        return getLaunchIntent(context, ranking.getId(), ranking.getName(), region);
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

    private void failure(final int errorCode) {
        mFullPlayer = null;
        mList = null;
        mMatchesBundle = null;
        mResult = null;
        showError(errorCode);
    }

    private void fetchData() {
        mRefreshLayout.setRefreshing(true);
        mDataListener = new DataListener();
        mDataListener.fetch();
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
    public FullPlayer getFullPlayer() {
        return mFullPlayer;
    }

    @Nullable
    @Override
    public MatchesBundle getMatchesBundle() {
        return mMatchesBundle;
    }

    @Nullable
    @Override
    public Match.Result getResult() {
        return mResult;
    }

    @Nullable
    @Override
    public CharSequence getSearchQuery() {
        return mPlayerToolbar.getSearchQuery();
    }

    @Override
    public void onClick(final MatchItemView v) {
        final Match match = v.getContent();
        startActivity(HeadToHeadActivity.getLaunchIntent(this, mFullPlayer, match));
    }

    @Override
    public void onClick(final TournamentDividerView v) {
        final AbsTournament tournament = v.getContent();
        startActivity(TournamentActivity.getLaunchIntent(this, tournament.getId(),
                tournament.getName(), tournament.getDate(), mRegionManager.getRegion(this)));
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);
        setContentView(R.layout.activity_player);

        final Intent intent = getIntent();
        mPlayerId = intent.getStringExtra(EXTRA_PLAYER_ID);

        setTitle();
        fetchData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataListener = null;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miAddToFavorites:
                mFavoritePlayersManager.addPlayer(mFullPlayer, mRegionManager.getRegion(this));
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

    @Override
    public void search(@Nullable final String query) {
        mThreadUtils.run(new ThreadUtils.Task() {
            private List<Object> mList;

            @Override
            public void onBackground() {
                if (!isAlive() || !TextUtils.equals(query, getSearchQuery())) {
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

    private void setTitle() {
        if (!TextUtils.isEmpty(getTitle())) {
            return;
        }

        if (mFullPlayer != null) {
            setTitle(mFullPlayer.getName());
        } else {
            final Intent intent = getIntent();

            if (intent.hasExtra(EXTRA_PLAYER_NAME)) {
                setTitle(intent.getStringExtra(EXTRA_PLAYER_NAME));
            }
        }

        setSubtitle(mRegionManager.getRegion(this).getDisplayName());
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
        mList = ListUtils.createPlayerMatchesList(this, mRegionManager, mFullPlayer,
                mMatchesBundle);
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
        setTitle();
        supportInvalidateOptionsMenu();
    }

    private void showError(final int errorCode) {
        mAdapter.clear();
        mRecyclerView.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE, errorCode);
        mRefreshLayout.setRefreshing(false);
        supportInvalidateOptionsMenu();
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    private void success(@NonNull final FullPlayer fullPlayer,
            @Nullable final MatchesBundle matchesBundle) {
        mFullPlayer = fullPlayer;
        mList = null;
        mMatchesBundle = matchesBundle;
        mResult = null;
        showData();
    }


    private final class DataListener {
        private boolean mFullPlayerFound;
        private boolean mMatchesBundleFound;
        private FullPlayer mFullPlayer;
        private int mFullPlayerErrorCode;
        private int mMatchesBundleErrorCode;
        private MatchesBundle mMatchesBundle;
        private final Region mRegion;

        private DataListener() {
            mRegion = mRegionManager.getRegion(PlayerActivity.this);
        }

        private void fetch() {
            mServerApi.getMatches(mRegion, mPlayerId, new ApiCall<>(mMatchesBundleListener));
            mServerApi.getPlayer(mRegion, mPlayerId, new ApiCall<>(mFullPlayerListener));
        }

        private synchronized void proceed() {
            if (mFullPlayerFound && mMatchesBundleFound) {
                // intentionally not checking to see if mMatchesBundle is null, PlayerActivity is
                // designed to handle a null MatchesBundle
                if (mFullPlayerErrorCode == Constants.ERROR_CODE_BAD_REQUEST ||
                        mMatchesBundleErrorCode == Constants.ERROR_CODE_BAD_REQUEST) {
                    failure(Constants.ERROR_CODE_BAD_REQUEST);
                } else if (mFullPlayer == null) {
                    failure(Constants.ERROR_CODE_UNKNOWN);
                } else {
                    success(mFullPlayer, mMatchesBundle);
                }
            }
        }

        private final ApiListener<FullPlayer> mFullPlayerListener = new ApiListener<FullPlayer>() {
            @Override
            public void failure(final int errorCode) {
                mFullPlayerErrorCode = errorCode;
                mFullPlayer = null;
                mFullPlayerFound = true;
                proceed();
            }

            @Override
            public boolean isAlive() {
                return PlayerActivity.this.isAlive();
            }

            @Override
            public void success(@Nullable final FullPlayer fullPlayer) {
                mFullPlayer = fullPlayer;
                mFullPlayerFound = true;
                proceed();
            }
        };

        private final ApiListener<MatchesBundle> mMatchesBundleListener = new ApiListener<MatchesBundle>() {
            @Override
            public void failure(final int errorCode) {
                mMatchesBundleErrorCode = errorCode;
                mMatchesBundle = null;
                mMatchesBundleFound = true;
                proceed();
            }

            @Override
            public boolean isAlive() {
                return PlayerActivity.this.isAlive();
            }

            @Override
            public void success(@Nullable final MatchesBundle matchesBundle) {
                mMatchesBundle = matchesBundle;
                mMatchesBundleFound = true;
                proceed();
            }
        };
    }

}
