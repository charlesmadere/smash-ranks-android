package com.garpr.android.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.garpr.android.R;
import com.garpr.android.models.FullTournament;
import com.garpr.android.views.TournamentMatchesView;
import com.garpr.android.views.TournamentPlayersView;

public class TournamentPagerAdapter extends PagerAdapter {

    public static final int POSITION_MATCHES = 0;
    public static final int POSITION_PLAYERS = 1;

    private final Context mContext;
    private final FullTournament mTournament;


    public TournamentPagerAdapter(@NonNull final Context context,
            @NonNull final FullTournament tournament) {
        mContext = context;
        mTournament = tournament;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
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
        switch (position) {
            case POSITION_MATCHES: {
                final TournamentMatchesView view = TournamentMatchesView.inflate(container);
                view.setContent(mTournament);
                return view;
            }

            case POSITION_PLAYERS: {
                final TournamentPlayersView view = TournamentPlayersView.inflate(container);
                view.setContent(mTournament);
                return view;
            }
        }

        throw new RuntimeException("illegal position: " + position);
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view == object;
    }

}
