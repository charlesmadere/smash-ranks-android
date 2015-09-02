package com.garpr.android.views;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.models.Tournament;


public class TournamentSeparatorView extends FrameLayout {


    private LinearLayout mContainer;
    private TextView mDate;
    private TextView mName;
    private Tournament mTournament;
    private ViewHolder mViewHolder;




    public static TournamentSeparatorView inflate(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (TournamentSeparatorView) inflater.inflate(R.layout.view_tournament_separator_item,
                parent, false);
    }


    public TournamentSeparatorView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    public Tournament getTournament() {
        return mTournament;
    }


    public ViewHolder getViewHolder() {
        if (mViewHolder == null) {
            mViewHolder = new ViewHolder();
        }

        return mViewHolder;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContainer = (LinearLayout) findViewById(R.id.view_tournament_separator_item_container);
        mDate = (TextView) findViewById(R.id.view_tournament_separator_item_date);
        mName = (TextView) findViewById(R.id.view_tournament_separator_item_name);
    }


    public void setOnClickListener(final OnClickListener l) {
        if (l == null) {
            mContainer.setClickable(false);
        } else {
            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    l.onClick(TournamentSeparatorView.this);
                }
            });
        }
    }


    public void setTournament(final Tournament tournament) {
        mTournament = tournament;
        mDate.setText(mTournament.getDateWrapper().getMmDdYy());
        mName.setText(mTournament.getName());
    }




    public final class ViewHolder extends RecyclerView.ViewHolder {


        private ViewHolder() {
            super(TournamentSeparatorView.this);
        }


        public TournamentSeparatorView getView() {
            return TournamentSeparatorView.this;
        }


    }


    public interface OnClickListener {


        void onClick(final TournamentSeparatorView v);


    }


}
