package com.garpr.android.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.garpr.android.R;
import com.garpr.android.misc.Searchable;
import com.garpr.android.models.FullTournament;
import com.garpr.android.views.TournamentMatchesLayout;
import com.garpr.android.views.TournamentPageLayout;
import com.garpr.android.views.TournamentPlayersLayout;

import java.lang.ref.WeakReference;

public class TournamentPagerAdapter extends PagerAdapter implements Searchable {

    private static final int POSITION_MATCHES = 0;
    private static final int POSITION_PLAYERS = 1;

    private final Context mContext;
    private final FullTournament mTournament;
    private final SparseArrayCompat<WeakReference<TournamentPageLayout>> mPages;


    public TournamentPagerAdapter(@NonNull final Context context,
            @NonNull final FullTournament tournament) {
        mContext = context;
        mTournament = tournament;
        mPages = new SparseArrayCompat<>(getCount());
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
        mPages.removeAt(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case POSITION_MATCHES: return mContext.getString(R.string.matches);
            case POSITION_PLAYERS: return mContext.getString(R.string.players);
        }

        throw new RuntimeException("illegal position: " + position);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final TournamentPageLayout view;

        switch (position) {
            case POSITION_MATCHES:
                view = TournamentMatchesLayout.inflate(container);
                break;

            case POSITION_PLAYERS:
                view = TournamentPlayersLayout.inflate(container);
                break;

            default:
                throw new RuntimeException("illegal position: " + position);
        }

        view.setContent(mTournament);
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
            final WeakReference<TournamentPageLayout> reference = mPages.get(i);

            if (reference != null) {
                final TournamentPageLayout view = reference.get();

                if (view != null && view.isAlive()) {
                    view.search(query);
                }
            }
        }
    }

}
