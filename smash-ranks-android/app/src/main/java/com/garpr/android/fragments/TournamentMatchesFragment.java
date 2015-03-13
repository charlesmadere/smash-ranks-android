package com.garpr.android.fragments;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.garpr.android.models.TournamentBundle;


public class TournamentMatchesFragment extends TournamentViewPagerFragment {


    private static final String TAG = "TournamentMatchesFragment";




    public static TournamentMatchesFragment create(final TournamentBundle bundle) {
        return (TournamentMatchesFragment) create(new TournamentMatchesFragment(), bundle);
    }


    @Override
    protected TournamentAdapter createAdapter() {
        return new TournamentMatchesAdapter();
    }


    @Override
    protected String getFragmentName() {
        return TAG;
    }




    private final class TournamentMatchesAdapter extends TournamentAdapter {


        @Override
        public String getAdapterName() {
            return null;
        }


        @Override
        public int getItemCount() {
            return 0;
        }


        @Override
        public long getItemId(final int position) {
            return 0;
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                final int viewType) {
            return null;
        }


    }


}
