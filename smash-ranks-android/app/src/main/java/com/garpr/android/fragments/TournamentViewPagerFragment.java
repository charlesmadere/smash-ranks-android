package com.garpr.android.fragments;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.garpr.android.R;
import com.garpr.android.misc.LinearLayoutManagerWrapper;
import com.garpr.android.misc.RecyclerAdapter;
import com.garpr.android.models.TournamentBundle;


public abstract class TournamentViewPagerFragment extends BaseFragment {


    private static final String KEY_BUNDLE = "KEY_BUNDLE";

    private RecyclerView mRecyclerView;
    private TournamentBundle mBundle;




    protected static TournamentViewPagerFragment create(final TournamentViewPagerFragment fragment,
            final TournamentBundle bundle) {
        final Bundle arguments = new Bundle();
        arguments.putParcelable(KEY_BUNDLE, bundle);
        fragment.setArguments(arguments);

        return fragment;
    }


    protected abstract TournamentAdapter createAdapter(final TournamentBundle bundle);


    private void findViews() {
        final View view = getView();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_tournament_view_pager_list);
    }


    @Override
    protected int getContentView() {
        return R.layout.fragment_tournament_view_pager;
    }


    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }


    protected TournamentBundle getTournamentBundle() {
        return mBundle;
    }


    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        readArguments();
        findViews();
        prepareList();
    }


    protected void prepareList() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManagerWrapper(getActivity()));
        mRecyclerView.setAdapter(createAdapter(mBundle));
    }


    protected void readArguments() {
        final Bundle arguments = getArguments();
        mBundle = arguments.getParcelable(KEY_BUNDLE);
    }




    protected abstract class TournamentAdapter<T extends RecyclerView.ViewHolder> extends
            RecyclerAdapter<T> {


        protected TournamentAdapter() {
            super(mRecyclerView);
        }


    }


}
