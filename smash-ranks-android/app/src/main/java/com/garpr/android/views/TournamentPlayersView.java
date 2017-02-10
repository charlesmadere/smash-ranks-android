package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.garpr.android.R;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.adapters.PlayersAdapter;
import com.garpr.android.models.FullTournament;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TournamentPlayersView extends FrameLayout implements BaseAdapterView<FullTournament> {

    private PlayersAdapter mAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty)
    View mEmpty;


    public static TournamentPlayersView inflate(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (TournamentPlayersView) inflater.inflate(R.layout.view_tournament_players, parent, false);
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
        ButterKnife.bind(this);

        mAdapter = new PlayersAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setContent(final FullTournament content) {
        if (content.hasPlayers()) {
            mAdapter.set(content.getPlayers());
            mEmpty.setVisibility(GONE);
            mRecyclerView.setVisibility(VISIBLE);
        } else {
            mAdapter.clear();
            mRecyclerView.setVisibility(GONE);
            mEmpty.setVisibility(VISIBLE);
        }
    }

}
