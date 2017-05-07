package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.DividerItemDecoration;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.garpr.android.R;
import com.garpr.android.adapters.PlayersAdapter;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.ThreadUtils;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.FullTournament;

import java.util.List;

public class TournamentPlayersLayout extends TournamentPageLayout {

    private FullTournament mContent;
    private PlayersAdapter mAdapter;


    public static TournamentPlayersLayout inflate(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (TournamentPlayersLayout) inflater.inflate(R.layout.layout_tournament_players,
                parent, false);
    }

    public TournamentPlayersLayout(@NonNull final Context context,
            @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public TournamentPlayersLayout(@NonNull final Context context,
            @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TournamentPlayersLayout(@NonNull final Context context,
            @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr,
            @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new PlayersAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void search(@Nullable final String query) {
        if (mContent == null || !mContent.hasPlayers()) {
            return;
        }

        mThreadUtils.run(new ThreadUtils.Task() {
            private List<AbsPlayer> mList;

            @Override
            public void onBackground() {
                if (!isAlive() || !TextUtils.equals(query, getSearchQuery())) {
                    return;
                }

                mList = ListUtils.searchPlayerList(query, mContent.getPlayers());
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

    @Override
    public void setContent(final FullTournament content) {
        mContent = content;

        if (mContent.hasPlayers()) {
            mAdapter.set(mContent.getPlayers());
            mEmpty.setVisibility(GONE);
            mRecyclerView.setVisibility(VISIBLE);
        } else {
            mAdapter.clear();
            mRecyclerView.setVisibility(GONE);
            mEmpty.setVisibility(VISIBLE);
        }
    }

}
