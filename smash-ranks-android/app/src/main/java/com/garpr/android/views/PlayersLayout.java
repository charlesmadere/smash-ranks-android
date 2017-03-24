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
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.PlayersAdapter;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.ThreadUtils;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.PlayersBundle;
import com.garpr.android.networking.ApiCall;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PlayersLayout extends SearchableFrameLayout implements ApiListener<PlayersBundle>,
        SwipeRefreshLayout.OnRefreshListener {

    private PlayersAdapter mAdapter;
    private PlayersBundle mPlayersBundle;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ServerApi mServerApi;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.empty)
    View mEmpty;

    @BindView(R.id.error)
    View mError;


    public PlayersLayout(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayersLayout(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlayersLayout(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void failure() {
        mPlayersBundle = null;
        showError();
    }

    private void fetchPlayersBundle() {
        mPlayersBundle = null;
        mRefreshLayout.setRefreshing(true);
        mServerApi.getPlayers(mRegionManager.getRegion(getContext()), new ApiCall<>(this));
    }

    @Nullable
    public PlayersBundle getPlayersBundle() {
        return mPlayersBundle;
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
        mAdapter = new PlayersAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        fetchPlayersBundle();
    }

    private void onPlayersBundleFetched() {
        if (!isAlive()) {
            return;
        }

        final Activity activity = MiscUtils.optActivity(getContext());

        if (activity instanceof Listener) {
            ((Listener) activity).onPlayersBundleFetched(this);
        }
    }

    @Override
    public void onRefresh() {
        fetchPlayersBundle();
    }

    @Override
    public void search(@Nullable final String query) {
        mThreadUtils.run(new ThreadUtils.Task() {
            private List<AbsPlayer> mList;

            @Override
            public void onBackground() {
                if (!isAlive()) {
                    return;
                }

                mList = ListUtils.searchPlayerList(query, mPlayersBundle.getPlayers());
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
        onPlayersBundleFetched();

        if (mPlayersBundle != null && mPlayersBundle.hasPlayers()) {
            showPlayersBundle();
        } else {
            showEmpty();
        }
    }


    public interface Listener {
        void onPlayersBundleFetched(final PlayersLayout layout);
    }

}
