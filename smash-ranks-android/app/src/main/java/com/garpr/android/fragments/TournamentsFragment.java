package com.garpr.android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garpr.android.R;
import com.garpr.android.adapters.TournamentsAdapter;
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
    private static final String KEY_TOURNAMENTS_BUNDLE = "TournamentsBundle";
    private static final String KEY_REGION = "Region";

    private TournamentsAdapter mAdapter;
    private TournamentsBundle mTournamentsBundle;
    private String mRegion;

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


    public static TournamentsFragment create(@NonNull final String region) {
        final Bundle args = new Bundle(1);
        args.putString(KEY_REGION, region);

        final TournamentsFragment fragment = new TournamentsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void failure() {
        mTournamentsBundle = null;
        showError();
    }

    private void fetchTournamentsBundle() {
        mRefreshLayout.setRefreshing(true);
        mServerApi.getTournaments(mRegion, new ApiCall<>(this));
    }

    @Override
    protected String getFragmentName() {
        return TAG;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            mTournamentsBundle = savedInstanceState.getParcelable(KEY_TOURNAMENTS_BUNDLE);
        }

        if (mTournamentsBundle == null) {
            fetchTournamentsBundle();
        } else if (mTournamentsBundle.hasTournaments()) {
            showTournamentsBundle();
        } else {
            showEmpty();
        }
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        mRegion = args.getString(KEY_REGION);
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
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mTournamentsBundle != null) {
            outState.putParcelable(KEY_TOURNAMENTS_BUNDLE, mTournamentsBundle);
        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRefreshLayout.setOnRefreshListener(this);
        mAdapter = new TournamentsAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showEmpty() {
        // TODO
    }

    private void showError() {
        // TODO
    }

    private void showTournamentsBundle() {
        // TODO
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
