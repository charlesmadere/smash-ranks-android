package com.garpr.android.adapters;

import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.garpr.android.activities.HomeActivity;
import com.garpr.android.misc.Searchable;
import com.garpr.android.views.FavoritePlayersLayout;
import com.garpr.android.views.RankingsLayout;
import com.garpr.android.views.SearchableFrameLayout;
import com.garpr.android.views.TournamentsLayout;

import java.lang.ref.WeakReference;

public class HomePagerAdapter extends PagerAdapter implements Searchable {

    private static final int POSITION_FAVORITE_PLAYERS = HomeActivity.POSITION_FAVORITE_PLAYERS;
    private static final int POSITION_RANKINGS = HomeActivity.POSITION_RANKINGS;
    private static final int POSITION_TOURNAMENTS = HomeActivity.POSITION_TOURNAMENTS;

    private final SparseArrayCompat<WeakReference<SearchableFrameLayout>> mPages;


    public HomePagerAdapter() {
        mPages = new SparseArrayCompat<>(getCount());
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
        mPages.removeAt(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final SearchableFrameLayout view;

        switch (position) {
            case POSITION_FAVORITE_PLAYERS:
                view = FavoritePlayersLayout.inflate(container);
                break;

            case POSITION_TOURNAMENTS:
                view = TournamentsLayout.inflate(container);
                break;

            case POSITION_RANKINGS:
                view = RankingsLayout.inflate(container);
                break;

            default:
                throw new RuntimeException("illegal position: " + position);
        }

        container.addView(view);
        mPages.put(position, new WeakReference<>(view));

        return view;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view == object;
    }

    @Override
    public void search(@Nullable final String query) {
        for (int i = 0; i < mPages.size(); ++i) {
            final WeakReference<SearchableFrameLayout> reference = mPages.get(i);

            if (reference != null) {
                final SearchableFrameLayout view = reference.get();

                if (view != null && view.isAlive()) {
                    view.search(query);
                }
            }
        }
    }

}
