package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.PlayersAdapter;
import com.garpr.android.misc.FavoritePlayersManager;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.ThreadUtils;
import com.garpr.android.models.AbsPlayer;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class FavoritePlayersLayout extends SearchableFrameLayout implements
        FavoritePlayersManager.OnFavoritePlayersChangeListener {

    private PlayersAdapter mAdapter;

    @Inject
    FavoritePlayersManager mFavoritePlayersManager;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty)
    View mEmpty;


    public static FavoritePlayersLayout inflate(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (FavoritePlayersLayout) inflater.inflate(R.layout.layout_favorite_players, parent,
                false);
    }

    public FavoritePlayersLayout(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public FavoritePlayersLayout(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FavoritePlayersLayout(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) {
            return;
        }

        mFavoritePlayersManager.addListener(this);
        refresh();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFavoritePlayersManager.addListener(this);
    }

    @Override
    public void onFavoritePlayersChanged(final FavoritePlayersManager manager) {
        if (ViewCompat.isAttachedToWindow(this)) {
            refresh();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (isInEditMode()) {
            return;
        }

        App.get().getAppComponent().inject(this);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new PlayersAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mFavoritePlayersManager.addListener(this);

        refresh();
    }

    private void refresh() {
        if (mFavoritePlayersManager.isEmpty()) {
            mAdapter.clear();
            mRecyclerView.setVisibility(GONE);
            mEmpty.setVisibility(VISIBLE);
        } else {
            mAdapter.set(mFavoritePlayersManager.getAbsPlayers());
            mEmpty.setVisibility(GONE);
            mRecyclerView.setVisibility(VISIBLE);
        }
    }

    @Override
    public void search(@Nullable final String query) {
        mThreadUtils.run(new ThreadUtils.Task() {
            private List<AbsPlayer> mList;

            @Override
            public void onBackground() {
                if (!isAlive() || !TextUtils.equals(query, getSearchQuery())) {
                    return;
                }

                mList = ListUtils.searchPlayerList(query, mFavoritePlayersManager.getAbsPlayers());
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

}
