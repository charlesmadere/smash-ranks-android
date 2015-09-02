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
import com.garpr.android.models.Player;

import java.text.NumberFormat;


public class RankingItemView extends FrameLayout {


    private LinearLayout mContainer;
    private Player mPlayer;
    private TextView mName;
    private TextView mRank;
    private TextView mRating;
    private ViewHolder mViewHolder;




    public static RankingItemView inflate(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (RankingItemView) inflater.inflate(R.layout.view_ranking_item, parent, false);
    }


    public RankingItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    public Player getPlayer() {
        return mPlayer;
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
        mContainer = (LinearLayout) findViewById(R.id.view_ranking_item_container);
        mName = (TextView) findViewById(R.id.view_ranking_item_name);
        mRank = (TextView) findViewById(R.id.view_ranking_item_rank);
        mRating = (TextView) findViewById(R.id.view_ranking_item_rating);
    }


    public void setOnClickListener(final OnClickListener l) {
        if (l == null) {
            mContainer.setClickable(false);
        } else {
            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    l.onClick(RankingItemView.this);
                }
            });
        }
    }


    public void setPlayer(final Player player) {
        mPlayer = player;
        mName.setText(mPlayer.getName());
        mRank.setText(NumberFormat.getInstance().format(mPlayer.getRank()));
        mRating.setText(mPlayer.getRatingTruncated());
    }




    public final class ViewHolder extends RecyclerView.ViewHolder {


        private ViewHolder() {
            super(RankingItemView.this);
        }


        public RankingItemView getView() {
            return RankingItemView.this;
        }


    }


    public interface OnClickListener {


        void onClick(final RankingItemView v);


    }


}
