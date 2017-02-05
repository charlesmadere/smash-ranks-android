package com.garpr.android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.PlayersAdapter;
import com.garpr.android.models.PlayersBundle;
import com.garpr.android.networking.ApiCall;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.views.RefreshLayout;

import javax.inject.Inject;

import butterknife.BindView;

public class PlayersFragment extends BaseFragment implements ApiListener<PlayersBundle>,
        SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "PlayersFragment";
    private static final String KEY_PLAYERS_BUNDLE = "PlayersBundle";
    private static final String KEY_REGION = "Region";

    private PlayersAdapter mAdapter;
    private PlayersBundle mPlayersBundle;
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


    public static PlayersFragment create(@NonNull final String region) {
        final Bundle args = new Bundle(1);
        args.putString(KEY_REGION, region);

        final PlayersFragment fragment = new PlayersFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void failure() {
        mPlayersBundle = null;
        showError();
    }

    private void fetchPlayersBundle() {
        mPlayersBundle = null;
        mRefreshLayout.setRefreshing(true);
        mServerApi.getPlayers(mRegion, new ApiCall<>(this));
    }

    @Override
    protected String getFragmentName() {
        return TAG;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            mPlayersBundle = savedInstanceState.getParcelable(KEY_PLAYERS_BUNDLE);
        }

        if (mPlayersBundle == null) {
            fetchPlayersBundle();
        } else if (mPlayersBundle.hasPlayers()) {
            showPlayersBundle();
        } else {
            showEmpty();
        }
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);

        final Bundle args = getArguments();
        mRegion = args.getString(KEY_REGION);
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
        fetchPlayersBundle();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mPlayersBundle != null) {
            outState.putParcelable(KEY_PLAYERS_BUNDLE, mPlayersBundle);
        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRefreshLayout.setOnRefreshListener(this);
        mAdapter = new PlayersAdapter(getContext());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.HORIZONTAL));
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

    private void showPlayersBundle() {
        mAdapter.set(mPlayersBundle);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void success(@Nullable final PlayersBundle playersBundle) {
        mPlayersBundle = playersBundle;

        if (mPlayersBundle != null && mPlayersBundle.hasPlayers()) {
            showPlayersBundle();
        } else {
            showEmpty();
        }
    }

}
