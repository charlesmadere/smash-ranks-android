package com.garpr.android.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.garpr.android.R;
import com.garpr.android.fragments.PlayersFragment;
import com.garpr.android.fragments.RankingsFragment;
import com.garpr.android.fragments.TournamentsFragment;

import butterknife.BindView;

public class HomeActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        FragmentManager.OnBackStackChangedListener {

    private static final String TAG = "HomeActivity";

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView mBottomNavigationView;


    @Override
    public String getActivityName() {
        return TAG;
    }

    private void navigateToTag(@NonNull final String tag) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final boolean fragmentExists = fragmentManager.findFragmentById(R.id.flContent) != null;
        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        if (fragment == null) {
            if (PlayersFragment.TAG.equals(tag)) {
                fragment = PlayersFragment.create();
            } else if (RankingsFragment.TAG.equals(tag)) {
                fragment = RankingsFragment.create();
            } else if (TournamentsFragment.TAG.equals(tag)) {
                fragment = TournamentsFragment.create();
            } else {
                throw new RuntimeException("unknown tag: " + tag);
            }

            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (fragmentExists) {
                fragmentTransaction.replace(R.id.flContent, fragment, tag);
                fragmentTransaction.addToBackStack(null);
            } else {
                fragmentTransaction.add(R.id.flContent, fragment, tag);
            }

            fragmentTransaction.commit();
        } else {
            fragmentManager.popBackStack(tag, 0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateSelectedNavigationItem();
    }

    @Override
    public void onBackStackChanged() {
        updateSelectedNavigationItem();
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            navigateToTag(RankingsFragment.TAG);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        if (item.isChecked()) {
            return false;
        }

        final String tag;

        switch (item.getItemId()) {
            case R.id.actionPlayers:
                tag = PlayersFragment.TAG;
                break;

            case R.id.actionRankings:
                tag = RankingsFragment.TAG;
                break;

            case R.id.actionTournaments:
                tag = TournamentsFragment.TAG;
                break;

            default:
                throw new RuntimeException("unknown item: " + item.getTitle());
        }

        navigateToTag(tag);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miSettings:
                startActivity(SettingsActivity.getLaunchIntent(this));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void updateSelectedNavigationItem() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment fragment = fragmentManager.findFragmentById(R.id.flContent);

        if (fragment instanceof PlayersFragment) {
            mBottomNavigationView.getMenu().findItem(R.id.actionPlayers).setChecked(true);
        } else if (fragment instanceof RankingsFragment) {
            mBottomNavigationView.getMenu().findItem(R.id.actionRankings).setChecked(true);
        } else if (fragment instanceof TournamentsFragment) {
            mBottomNavigationView.getMenu().findItem(R.id.actionTournaments).setChecked(true);
        }
    }

}
