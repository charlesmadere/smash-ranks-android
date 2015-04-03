package com.garpr.android.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;


public class PlayerItemView extends FrameLayout {


    private ViewHolder mViewHolder;




    public PlayerItemView(final Context context) {
        super(context);
    }


    public PlayerItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    public PlayerItemView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlayerItemView(final Context context, final AttributeSet attrs, final int defStyleAttr,
            final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public ViewHolder getViewHolder() {
        if (mViewHolder == null) {
            mViewHolder = new ViewHolder();
        }

        return mViewHolder;
    }




    public final class ViewHolder extends RecyclerView.ViewHolder {


        private ViewHolder() {
            super(PlayerItemView.this);
        }


        public PlayerItemView getView() {
            return PlayerItemView.this;
        }


    }


}
