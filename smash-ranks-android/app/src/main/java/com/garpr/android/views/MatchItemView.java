package com.garpr.android.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.models.Match;
import com.garpr.android.models.Player;


public class MatchItemView extends FrameLayout implements View.OnClickListener,
        View.OnLongClickListener {


    private LinearLayout mContainer;
    private Match mMatch;
    private OnClickListener mClickListener;
    private OnLongClickListener mLongClickListener;
    private TextView mLoser;
    private TextView mWinner;
    private ViewHolder mViewHolder;




    public MatchItemView(final Context context) {
        super(context);
    }


    public MatchItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    public MatchItemView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MatchItemView(final Context context, final AttributeSet attrs, final int defStyleAttr,
            final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public LinearLayout getContainerView() {
        return mContainer;
    }


    public TextView getLoserView() {
        return mLoser;
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


    public TextView getWinnerView() {
        return mWinner;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContainer = (LinearLayout) findViewById(R.id.view_match_item_container);
        mLoser = (TextView) findViewById(R.id.view_match_item_loser);
        mWinner = (TextView) findViewById(R.id.view_match_item_winner);

        mContainer.setOnClickListener(this);
        mContainer.setOnLongClickListener(this);
    }


    @Override
    public void onClick(final View v) {
        if (mClickListener != null) {
            mClickListener.onClick(this);
        }
    }


    @Override
    public boolean onLongClick(final View v) {
        final boolean clickConsumed;

        if (mLongClickListener == null) {
            clickConsumed = false;
        } else {
            mLongClickListener.onLongClick(this);
            clickConsumed = true;
        }

        return clickConsumed;
    }


    public void setMatch(final Match match) {
        mMatch = match;

        final Player loser = match.getLoser();
        mLoser.setText(loser.getName());

        final Player winner = mMatch.getWinner();
        mWinner.setText(winner.getName());
    }


    public void setOnClickListener(final OnClickListener l) {
        mClickListener = l;
    }


    public void setOnLongClickListener(final OnLongClickListener l) {
        mLongClickListener = l;
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


        void onLongClick(final MatchItemView v);


    }


}
