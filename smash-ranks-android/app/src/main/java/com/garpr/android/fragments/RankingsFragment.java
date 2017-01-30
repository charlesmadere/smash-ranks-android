package com.garpr.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garpr.android.R;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.views.RefreshLayout;

import javax.inject.Inject;

import butterknife.BindView;

public class RankingsFragment extends BaseFragment implements ApiListener<RankingsBundle>,
        SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "RankingsFragment";

    @Inject
    ServerApi mServerApi;

    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    @BindView(R.id.empty)
    View mEmpty;

    @BindView(R.id.error)
    View mError;


    public static RankingsFragment create() {
        return new RankingsFragment();
    }

    @Override
    public void failure() {
        // TODO
    }

    @Override
    protected String getFragmentName() {
        return TAG;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_rankings, container, false);
    }

    @Override
    public void onRefresh() {
        // TODO
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void success(@Nullable final RankingsBundle rankingsBundle) {
        // TODO
    }

}
