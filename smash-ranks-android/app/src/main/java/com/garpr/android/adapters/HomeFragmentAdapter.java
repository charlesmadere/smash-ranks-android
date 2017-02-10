package com.garpr.android.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.garpr.android.fragments.PlayersFragment;
import com.garpr.android.fragments.RankingsFragment;
import com.garpr.android.fragments.TournamentsFragment;

public class HomeFragmentAdapter extends FragmentStatePagerAdapter {

    public static final int POSITION_RANKINGS = 0;
    public static final int POSITION_PLAYERS = 1;
    public static final int POSITION_TOURNAMENTS = 2;


    public HomeFragmentAdapter(final FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case POSITION_RANKINGS: return RankingsFragment.create();
            case POSITION_PLAYERS: return PlayersFragment.create();
            case POSITION_TOURNAMENTS: return TournamentsFragment.create();
        }

        throw new RuntimeException("illegal position: " + position);
    }

}
