package com.garpr.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.TournamentsAdapter;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.models.TournamentsBundle;
import com.garpr.android.networking.ApiCall;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.views.RefreshLayout;

import javax.inject.Inject;

import butterknife.BindView;

public class TournamentsFragment extends BaseFragment implements ApiListener<TournamentsBundle>,
        SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "TournamentsFragment";

    private TournamentsAdapter mAdapter;
    private TournamentsBundle mTournamentsBundle;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ServerApi mServerApi;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    @BindView(R.id.empty)
    View mEmpty;

    @BindView(R.id.error)
    View mError;


    public static TournamentsFragment create() {
        return new TournamentsFragment();
    }

    @Override
    public void failure() {
        mTournamentsBundle = null;
        showError();
    }

    private void fetchTournamentsBundle() {
        mTournamentsBundle = null;
        mRefreshLayout.setRefreshing(true);
        mServerApi.getTournaments(mRegionManager.getRegion(getContext()), new ApiCall<>(this));
    }

    @Override
    protected String getFragmentName() {
        return TAG;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fetchTournamentsBundle();
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_tournaments, container, false);
    }

    @Override
    public void onRefresh() {
        fetchTournamentsBundle();
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        mAdapter = new TournamentsAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showEmpty() {
        mRecyclerView.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mEmpty.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
    }

    private void showError() {
        mAdapter.clear();
        mRecyclerView.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
    }

    private void showTournamentsBundle() {
        mAdapter.set(ListUtils.createTournamentList(mTournamentsBundle));
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void success(@Nullable final TournamentsBundle tournamentsBundle) {
        mTournamentsBundle = tournamentsBundle;

        if (mTournamentsBundle != null && mTournamentsBundle.hasTournaments()) {
            showTournamentsBundle();
        } else {
            showEmpty();
        }
    }

}
