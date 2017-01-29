package com.garpr.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garpr.android.R;
import com.garpr.android.views.RefreshLayout;

import butterknife.BindView;

public class RankingsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "RankingsFragment";

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
    protected String getFragmentName() {
        return TAG;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_players, container, false);
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

}
