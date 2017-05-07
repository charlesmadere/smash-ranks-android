package com.garpr.android.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.RankingsAdapter;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.Refreshable;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.ThreadUtils;
import com.garpr.android.models.Ranking;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.networking.ApiCall;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class RankingsLayout extends SearchableFrameLayout implements ApiListener<RankingsBundle>,
        Refreshable, SwipeRefreshLayout.OnRefreshListener {

    private RankingsAdapter mAdapter;
    private RankingsBundle mRankingsBundle;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ServerApi mServerApi;

    @Inject
    ThreadUtils mThreadUtils;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.empty)
    View mEmpty;

    @BindView(R.id.error)
    View mError;


    public static RankingsLayout inflate(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (RankingsLayout) inflater.inflate(R.layout.layout_rankings, parent, false);
    }

    public RankingsLayout(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public RankingsLayout(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RankingsLayout(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void failure(final int errorCode) {
        mRankingsBundle = null;
        onRankingsBundleFetched();
        showError();
    }

    private void fetchRankingsBundle() {
        mRankingsBundle = null;
        mRefreshLayout.setRefreshing(true);
        mServerApi.getRankings(mRegionManager.getRegion(getContext()), new ApiCall<>(this));
    }

    @Nullable
    public RankingsBundle getRankingsBundle() {
        return mRankingsBundle;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (isInEditMode()) {
            return;
        }

        App.get().getAppComponent().inject(this);

        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new RankingsAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        fetchRankingsBundle();
    }

    private void onRankingsBundleFetched() {
        if (!isAlive()) {
            return;
        }

        final Activity activity = MiscUtils.optActivity(getContext());

        if (activity instanceof Listener) {
            ((Listener) activity).onRankingsBundleFetched(this);
        }
    }

    @Override
    public void onRefresh() {
        fetchRankingsBundle();
    }

    @Override
    public void refresh() {
        fetchRankingsBundle();
    }

    @Override
    public void search(@Nullable final String query) {
        mThreadUtils.run(new ThreadUtils.Task() {
            private List<Ranking> mList;

            @Override
            public void onBackground() {
                if (!isAlive() || !TextUtils.equals(query, getSearchQuery())) {
                    return;
                }

                mList = ListUtils.searchRankingList(query, mRankingsBundle.getRankings());
            }

            @Override
            public void onUi() {
                if (!isAlive() || !TextUtils.equals(query, getSearchQuery())) {
                    return;
                }

                mAdapter.set(mList);
            }
        });
    }

    private void showEmpty() {
        mAdapter.clear();
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
        onRankingsBundleFetched();

        if (mRankingsBundle != null && mRankingsBundle.hasRankings()) {
            showRankingsBundle();
        } else {
            showEmpty();
        }
    }


    public interface Listener {
        void onRankingsBundleFetched(final RankingsLayout layout);
    }

}
