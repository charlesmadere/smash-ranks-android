package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.SearchQueryHandle;
import com.garpr.android.models.PlayersBundle;
import com.garpr.android.views.PlayersLayout;

import javax.inject.Inject;

import butterknife.BindView;

public class PlayersActivity extends BaseActivity implements MenuItemCompat.OnActionExpandListener,
        PlayersLayout.Listener, SearchQueryHandle, SearchView.OnQueryTextListener {

    private static final String TAG = "PlayersActivity";

    private SearchView mSearchView;

    @Inject
    RegionManager mRegionManager;

    @BindView(R.id.playersLayout)
    PlayersLayout mPlayersLayout;


    public static Intent getLaunchIntent(final Context context) {
        return getLaunchIntent(context, null);
    }

    public static Intent getLaunchIntent(final Context context, @Nullable final String region) {
        final Intent intent = new Intent(context, PlayersActivity.class);

        if (!TextUtils.isEmpty(region)) {
            intent.putExtra(EXTRA_REGION, region);
        }

        return intent;
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
        setContentView(R.layout.activity_players);
        setSubtitle(mRegionManager.getRegion(this));
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_players, menu);

        final PlayersBundle playersBundle = mPlayersLayout.getPlayersBundle();
        if (playersBundle != null && playersBundle.hasPlayers()) {
            final MenuItem searchMenuItem = menu.findItem(R.id.miSearch);
            searchMenuItem.setVisible(true);

            MenuItemCompat.setOnActionExpandListener(searchMenuItem, this);
            mSearchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
            mSearchView.setQueryHint(getText(R.string.search_players_));
            mSearchView.setOnQueryTextListener(this);
        }

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
    public void onPlayersBundleFetched(final PlayersLayout layout) {
        supportInvalidateOptionsMenu();
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

    private void search(@Nullable final String query) {
        mPlayersLayout.search(query);
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

}
