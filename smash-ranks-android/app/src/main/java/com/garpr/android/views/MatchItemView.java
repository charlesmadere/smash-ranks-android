package com.garpr.android.views;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.models.Match;
import com.garpr.android.models.Player;


public class MatchItemView extends FrameLayout {


    private Match mMatch;
    private TextView mLoser;
    private TextView mWinner;
    private ViewHolder mViewHolder;




    public static MatchItemView inflate(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (MatchItemView) inflater.inflate(R.layout.view_match_item, parent, false);
    }


    public MatchItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    public Match getMatch() {
        return mMatch;
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
        mLoser = (TextView) findViewById(R.id.view_match_item_loser);
        mWinner = (TextView) findViewById(R.id.view_match_item_winner);
    }


    public void setMatch(final Match match) {
        mMatch = match;

        final Player loser = match.getLoser();
        mLoser.setText(loser.getName());

        final Player winner = mMatch.getWinner();
        mWinner.setText(winner.getName());
    }


    public void setOnClickListener(final OnClickListener l) {
        if (l == null) {
            setClickable(false);
        } else {
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    l.onClick(MatchItemView.this);
                }
            });
        }
    }


    public void setOnLongClickListener(final OnLongClickListener l) {
        if (l == null) {
            setLongClickable(false);
        } else {
            setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    return l.onLongClick(MatchItemView.this);
                }
            });
        }
    }




    public final class ViewHolder extends RecyclerView.ViewHolder {


        private ViewHolder() {
            super(MatchItemView.this);
        }


        public MatchItemView getView() {
            return MatchItemView.this;
        }


    }


    public interface OnClickListener {


        void onClick(final MatchItemView v);


    }


    public interface OnLongClickListener {


        boolean onLongClick(final MatchItemView v);


    }


}
