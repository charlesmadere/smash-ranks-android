package com.garpr.android.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.PlayersSelectionAdapter;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.ResultCodes;
import com.garpr.android.misc.SearchQueryHandle;
import com.garpr.android.misc.ThreadUtils;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.PlayersBundle;
import com.garpr.android.networking.ApiCall;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.views.PlayerSelectionItemView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class SetIdentityActivity extends BaseActivity implements ApiListener<PlayersBundle>,
        MenuItemCompat.OnActionExpandListener, PlayerSelectionItemView.Listeners,
        SearchQueryHandle, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "SetIdentityActivity";

    private AbsPlayer mSelectedPlayer;
    private MenuItem mSaveMenuItem;
    private MenuItem mSearchMenuItem;
    private PlayersBundle mPlayersBundle;
    private PlayersSelectionAdapter mAdapter;
    private SearchView mSearchView;

    @Inject
    IdentityManager mIdentityManager;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ServerApi mServerApi;

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


    public static Intent getLaunchIntent(final Context context) {
        return new Intent(context, SetIdentityActivity.class);
    }

    @Override
    public void failure() {
        mSelectedPlayer = null;
        mPlayersBundle = null;
        showError();
    }

    private void fetchPlayersBundle() {
        mSelectedPlayer = null;
        mPlayersBundle = null;
        mRefreshLayout.setRefreshing(true);
        mServerApi.getPlayers(mRegionManager.getRegion(this), new ApiCall<>(this));
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

    @Nullable
    @Override
    public AbsPlayer getSelectedPlayer() {
        if (mSelectedPlayer == null) {
            return mIdentityManager.getIdentity();
        } else {
            return mSelectedPlayer;
        }
    }

    @Override
    protected void navigateUp() {
        if (mSelectedPlayer == null) {
            super.navigateUp();
            return;
        }

        new AlertDialog.Builder(this)
                .setMessage(R.string.youve_selected_an_identity_but_havent_saved)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        SetIdentityActivity.super.navigateUp();
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        if (mSelectedPlayer == null) {
            super.onBackPressed();
            return;
        }

        if (mSearchMenuItem != null && MenuItemCompat.isActionViewExpanded(mSearchMenuItem)) {
            MenuItemCompat.collapseActionView(mSearchMenuItem);
            return;
        }

        new AlertDialog.Builder(this)
                .setMessage(R.string.youve_selected_an_identity_but_havent_saved)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        SetIdentityActivity.super.onBackPressed();
                    }
                })
                .show();
    }

    @Override
    public void onClick(final PlayerSelectionItemView v) {
        final AbsPlayer player = v.getContent();

        if (player.equals(mIdentityManager.getIdentity())) {
            mSelectedPlayer = null;
        } else {
            mSelectedPlayer = player;
        }

        refreshMenu();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);
        setContentView(R.layout.activity_set_identity);
        setSubtitle(mRegionManager.getRegion(this).getDisplayName());

        fetchPlayersBundle();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_set_identity, menu);

        mSearchMenuItem = menu.findItem(R.id.miSearch);
        MenuItemCompat.setOnActionExpandListener(mSearchMenuItem, this);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);
        mSearchView.setQueryHint(getText(R.string.search_players_));
        mSearchView.setOnQueryTextListener(this);

        mSaveMenuItem = menu.findItem(R.id.miSave);
        refreshMenu();

        return super.onCreateOptionsMenu(menu);
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
            case R.id.miSave:
                save();
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
        fetchPlayersBundle();
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();

        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new PlayersSelectionAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void refreshMenu() {
        if (mSaveMenuItem == null || mSearchMenuItem == null || mSearchView == null) {
            return;
        }

        if (mRefreshLayout.isRefreshing() || mAdapter.isEmpty() ||
                mEmpty.getVisibility() == View.VISIBLE || mError.getVisibility() == View.VISIBLE) {
            mSaveMenuItem.setEnabled(false);
            mSaveMenuItem.setVisible(false);
            MenuItemCompat.collapseActionView(mSearchMenuItem);
            mSearchMenuItem.setVisible(false);
        } else {
            mSaveMenuItem.setEnabled(mSelectedPlayer != null);
            mSaveMenuItem.setVisible(true);
            mSearchMenuItem.setVisible(true);
        }
    }

    private void save() {
        mIdentityManager.setIdentity(mSelectedPlayer);
        Toast.makeText(this, R.string.identity_saved_, Toast.LENGTH_LONG).show();
        setResult(ResultCodes.IDENTITY_SELECTED.mValue);
        supportFinishAfterTransition();
    }

    private void search(@Nullable final String query) {
        mThreadUtils.run(new ThreadUtils.Task() {
            private List<AbsPlayer> mList;

            @Override
            public void onBackground() {
                if (!isAlive()) {
                    return;
                }

                mList = ListUtils.searchPlayerList(query, mPlayersBundle.getPlayers());
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

    private void showEmpty() {
        mAdapter.clear();
        mRecyclerView.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mEmpty.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
        refreshMenu();
    }

    private void showError() {
        mAdapter.clear();
        mRecyclerView.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
        refreshMenu();
    }

    private void showPlayersBundle() {
        mAdapter.set(mPlayersBundle);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
        mRefreshLayout.setEnabled(false);
        refreshMenu();
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    @Override
    public void success(@Nullable final PlayersBundle playersBundle) {
        mPlayersBundle = playersBundle;

        if (mPlayersBundle != null && mPlayersBundle.hasPlayers()) {
            showPlayersBundle();
        } else {
            showEmpty();
        }
    }

}
