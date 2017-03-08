package com.garpr.android.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import com.garpr.android.activities.HomeActivity;
import com.garpr.android.fragments.BaseSearchableFragment;
import com.garpr.android.fragments.FavoritePlayersFragment;
import com.garpr.android.fragments.RankingsFragment;
import com.garpr.android.fragments.TournamentsFragment;
import com.garpr.android.misc.Searchable;

import java.lang.ref.WeakReference;

public class HomeFragmentAdapter extends FragmentStatePagerAdapter implements Searchable {

    private static final int POSITION_FAVORITE_PLAYERS = HomeActivity.POSITION_FAVORITE_PLAYERS;
    private static final int POSITION_RANKINGS = HomeActivity.POSITION_RANKINGS;
    private static final int POSITION_TOURNAMENTS = HomeActivity.POSITION_TOURNAMENTS;

    private final SparseArrayCompat<WeakReference<BaseSearchableFragment>> mFragments;


    public HomeFragmentAdapter(final FragmentManager fm) {
        super(fm);
        mFragments = new SparseArrayCompat<>(getCount());
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        super.destroyItem(container, position, object);
        mFragments.removeAt(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(final int position) {
        final BaseSearchableFragment fragment;

        switch (position) {
            case POSITION_FAVORITE_PLAYERS:
                fragment = FavoritePlayersFragment.create();
                break;

            case POSITION_RANKINGS:
                fragment = RankingsFragment.create();
                break;

            case POSITION_TOURNAMENTS:
                fragment = TournamentsFragment.create();
                break;

            default:
                throw new RuntimeException("illegal position: " + position);
        }

        mFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final BaseSearchableFragment fragment = (BaseSearchableFragment) super.instantiateItem(
                container, position);
        mFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void search(@Nullable final String query) {
        for (int i = 0; i < mFragments.size(); ++i) {
            final WeakReference<BaseSearchableFragment> reference = mFragments.get(i);

            if (reference != null) {
                final BaseSearchableFragment fragment = reference.get();

                if (fragment != null && fragment.isAlive()) {
                    fragment.search(query);
                }
            }
        }
    }

}
