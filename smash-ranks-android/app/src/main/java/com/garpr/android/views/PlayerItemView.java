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
import com.garpr.android.models.Player;


public class PlayerItemView extends FrameLayout {


    private Player mPlayer;
    private TextView mName;
    private ViewHolder mViewHolder;




    public static PlayerItemView inflate(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (PlayerItemView) inflater.inflate(R.layout.view_player_item, parent, false);
    }


    public PlayerItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    public TextView getNameView() {
        return mName;
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
        mName = (TextView) findViewById(R.id.view_player_item_name);
    }


    public void setOnClickListener(final OnClickListener l) {
        if (l == null) {
            setClickable(false);
        } else {
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    l.onClick(PlayerItemView.this);
                }
            });
        }
    }


    public void setPlayer(final Player player) {
        mPlayer = player;
        mName.setText(mPlayer.getName());
    }




    public final class ViewHolder extends RecyclerView.ViewHolder {


        private ViewHolder() {
            super(PlayerItemView.this);
        }


        public PlayerItemView getView() {
            return PlayerItemView.this;
        }


    }


    public interface OnClickListener {


        void onClick(final PlayerItemView v);


    }


}
