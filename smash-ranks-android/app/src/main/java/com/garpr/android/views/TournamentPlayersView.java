package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
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

public class TournamentPlayersView extends TournamentPageView {

    private FullTournament mContent;
    private PlayersAdapter mAdapter;


    public static TournamentPlayersView inflate(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (TournamentPlayersView) inflater.inflate(R.layout.view_tournament_players, parent,
                false);
    }

    public TournamentPlayersView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public TournamentPlayersView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TournamentPlayersView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mAdapter = new PlayersAdapter(getContext());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void search(@Nullable final String query) {
        if (!mContent.hasPlayers()) {
            return;
        }

        mThreadUtils.run(new ThreadUtils.Task() {
            private List<AbsPlayer> mList;

            @Override
            public void onBackground() {
                if (!isAlive()) {
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
