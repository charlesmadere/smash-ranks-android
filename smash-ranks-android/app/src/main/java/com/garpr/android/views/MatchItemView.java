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
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.models.Match;
import com.garpr.android.models.Player;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MatchItemView extends FrameLayout {


    @Bind(R.id.view_match_item_loser)
    TextView mLoser;

    @Bind(R.id.view_match_item_winner)
    TextView mWinner;

    private Match mMatch;
    private ViewHolder mViewHolder;




    public static MatchItemView inflate(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (MatchItemView) inflater.inflate(R.layout.view_match_item, parent, false);
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
        ButterKnife.bind(this);
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




    public class ViewHolder extends RecyclerView.ViewHolder {


        protected ViewHolder() {
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
