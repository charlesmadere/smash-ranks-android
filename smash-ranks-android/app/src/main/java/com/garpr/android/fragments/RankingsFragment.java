package com.garpr.android.fragments;

import android.content.Context;
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
import com.garpr.android.adapters.RankingsAdapter;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.networking.ApiCall;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.views.RefreshLayout;

import javax.inject.Inject;

import butterknife.BindView;

public class RankingsFragment extends BaseFragment implements ApiListener<RankingsBundle>,
        SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "RankingsFragment";

    private Listener mListener;
    private RankingsAdapter mAdapter;
    private RankingsBundle mRankingsBundle;

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


    public static RankingsFragment create() {
        return new RankingsFragment();
    }

    @Override
    public void failure() {
        mRankingsBundle = null;
        mListener.onRankingsBundleFetched(null);
        showError();
    }

    private void fetchRankingsBundle() {
        mRankingsBundle = null;
        mRefreshLayout.setRefreshing(true);
        mServerApi.getRankings(mRegionManager.getRegion(getContext()), new ApiCall<>(this));
    }

    @Override
    protected String getFragmentName() {
        return TAG;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fetchRankingsBundle();
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        mListener = (Listener) MiscUtils.getActivity(context);
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
        return inflater.inflate(R.layout.fragment_rankings, container, false);
    }

    @Override
    public void onRefresh() {
        fetchRankingsBundle();
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRefreshLayout.setOnRefreshListener(this);
        mAdapter = new RankingsAdapter(getContext());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
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

    private void showRankingsBundle() {
        mAdapter.set(mRankingsBundle);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void success(@Nullable final RankingsBundle rankingsBundle) {
        mRankingsBundle = rankingsBundle;

        if (mRankingsBundle != null && mRankingsBundle.hasRankings()) {
            mListener.onRankingsBundleFetched(mRankingsBundle);
            showRankingsBundle();
        } else {
            mListener.onRankingsBundleFetched(null);
            showEmpty();
        }
    }


    public interface Listener {
        void onRankingsBundleFetched(@Nullable final RankingsBundle rankingsBundle);
    }

}
