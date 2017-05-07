package com.garpr.android.views;

import android.annotation.TargetApi;
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
import com.garpr.android.adapters.TournamentsAdapter;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.Refreshable;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.ThreadUtils;
import com.garpr.android.models.AbsTournament;
import com.garpr.android.models.TournamentsBundle;
import com.garpr.android.networking.ApiCall;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TournamentsLayout extends SearchableFrameLayout implements
        ApiListener<TournamentsBundle>, Refreshable, SwipeRefreshLayout.OnRefreshListener {

    private TournamentsAdapter mAdapter;
    private TournamentsBundle mTournamentsBundle;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ServerApi mServerApi;

    @Inject
    ThreadUtils mThreadUtils;

    @BindView(R.id.error)
    ErrorLinearLayout mError;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.empty)
    View mEmpty;


    public static TournamentsLayout inflate(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (TournamentsLayout) inflater.inflate(R.layout.layout_tournaments, parent, false);
    }

    public TournamentsLayout(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public TournamentsLayout(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TournamentsLayout(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void failure(final int errorCode) {
        mTournamentsBundle = null;
        showError(errorCode);
    }

    private void fetchTournamentsBundle() {
        mTournamentsBundle = null;
        mRefreshLayout.setRefreshing(true);
        mServerApi.getTournaments(mRegionManager.getRegion(getContext()), new ApiCall<>(this));
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
        mAdapter = new TournamentsAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        fetchTournamentsBundle();
    }

    @Override
    public void onRefresh() {
        fetchTournamentsBundle();
    }

    @Override
    public void refresh() {
        fetchTournamentsBundle();
    }

    @Override
    public void search(@Nullable final String query) {
        mThreadUtils.run(new ThreadUtils.Task() {
            private List<AbsTournament> mList;

            @Override
            public void onBackground() {
                if (!isAlive() || !TextUtils.equals(query, getSearchQuery())) {
                    return;
                }

                mList = ListUtils.searchTournamentList(query, mTournamentsBundle.getTournaments());
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

    private void showError(final int errorCode) {
        mAdapter.clear();
        mRecyclerView.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE, errorCode);
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
