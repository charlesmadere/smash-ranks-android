package com.garpr.android.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import com.garpr.android.activities.HomeActivity;
import com.garpr.android.fragments.BaseFragment;
import com.garpr.android.fragments.PlayersFragment;
import com.garpr.android.fragments.RankingsFragment;
import com.garpr.android.fragments.TournamentsFragment;
import com.garpr.android.misc.Searchable;

import java.lang.ref.WeakReference;

public class HomeFragmentAdapter extends FragmentStatePagerAdapter implements Searchable {

    private static final int POSITION_PLAYERS = HomeActivity.POSITION_PLAYERS;
    private static final int POSITION_RANKINGS = HomeActivity.POSITION_RANKINGS;
    private static final int POSITION_TOURNAMENTS = HomeActivity.POSITION_TOURNAMENTS;

    private final SparseArrayCompat<WeakReference<BaseFragment>> mFragments;


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
    public BaseFragment getItem(final int position) {
        final BaseFragment fragment;

        switch (position) {
            case POSITION_PLAYERS:
                fragment = PlayersFragment.create();
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
    public BaseFragment instantiateItem(final ViewGroup container, final int position) {
        final BaseFragment fragment = (BaseFragment) super.instantiateItem(container, position);
        mFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void search(@Nullable final String query) {
        for (int i = 0; i < mFragments.size(); ++i) {
            final WeakReference<BaseFragment> reference = mFragments.get(i);

            if (reference != null) {
                final BaseFragment fragment = reference.get();

                if (fragment != null && fragment.isAlive() && fragment instanceof Searchable) {
                    ((Searchable) fragment).search(query);
                }
            }
        }
    }

}
