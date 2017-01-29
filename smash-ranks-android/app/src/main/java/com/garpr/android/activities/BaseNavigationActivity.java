package com.garpr.android.activities;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.garpr.android.R;

import butterknife.BindView;

public abstract class BaseNavigationActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.bottomNavigationView)
    protected BottomNavigationView mBottomNavigationView;


    @IdRes
    protected int getSelectedNavigationItem() {
        return 0;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        if (item.isChecked()) {
            return false;
        }

        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.actionPlayers:
                intent = PlayersActivity.getLaunchIntent(this);
                break;

            case R.id.actionRankings:
                intent = RankingsActivity.getLaunchIntent(this);
                break;

            case R.id.actionTournaments:
                intent = TournamentsActivity.getLaunchIntent(this);
                break;
        }

        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(0, 0);
        }

        return false;
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        final int selectedNavigationItem = getSelectedNavigationItem();
        if (selectedNavigationItem != 0) {
            mBottomNavigationView.getMenu().findItem(selectedNavigationItem).setChecked(true);
        }
    }

}
