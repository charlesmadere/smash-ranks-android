package com.garpr.android.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.garpr.android.R;

import butterknife.BindView;

public abstract class BaseNavigationActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.bottomNavigationView)
    protected BottomNavigationView mBottomNavigationView;


    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionPlayers:
                onPlayersSelected();
                break;

            case R.id.actionRankings:
                onRankingsSelected();
                break;

            case R.id.actionTournaments:
                onTournamentsSelected();
                break;
        }

        return false;
    }

    protected void onPlayersSelected() {
        startActivity(PlayersActivity.getLaunchIntent(this));
    }

    protected void onRankingsSelected() {
        startActivity(RankingsActivity.getLaunchIntent(this));
    }

    protected void onTournamentsSelected() {
        startActivity(TournamentsActivity.getLaunchIntent(this));
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

}
