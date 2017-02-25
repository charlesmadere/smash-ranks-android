package com.garpr.android.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.PlayersSelectionAdapter;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.views.PlayerSelectionItemView;

import javax.inject.Inject;

public class SetIdentityActivity extends BaseActivity implements
        MenuItemCompat.OnActionExpandListener, PlayerSelectionItemView.Listeners,
        SearchView.OnQueryTextListener {

    private static final String TAG = "SetIdentityActivity";

    private AbsPlayer mSelectedPlayer;
    private PlayersSelectionAdapter mAdapter;

    @Inject
    IdentityManager mIdentityManager;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ServerApi mServerApi;


    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Nullable
    @Override
    public AbsPlayer getSelectedPlayer() {
        return mSelectedPlayer;
    }

    @Override
    public void onBackPressed() {
        if (mSelectedPlayer == null) {
            super.onBackPressed();
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
        mSelectedPlayer = v.getContent();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);
        setContentView(R.layout.activity_set_identity);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_set_identity, menu);

        // TODO

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

    private void search(@Nullable final String query) {
        // TODO
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

}
