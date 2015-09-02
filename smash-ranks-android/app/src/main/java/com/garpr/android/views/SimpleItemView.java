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


public class SimpleItemView extends FrameLayout {


    private TextView mText;
    private ViewHolder mViewHolder;




    public static SimpleItemView inflate(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (SimpleItemView) inflater.inflate(R.layout.view_simple_item, parent, false);
    }


    public SimpleItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
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
        mText = (TextView) findViewById(R.id.view_simple_item_text);
    }


    public void setOnClickListener(final OnClickListener l) {
        if (l == null) {
            setClickable(false);
        } else {
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    l.onClick(SimpleItemView.this);
                }
            });
        }
    }


    public void setText(final CharSequence text) {
        mText.setText(text);
    }


    public void setText(final int resId) {
        mText.setText(resId);
    }




    public final class ViewHolder extends RecyclerView.ViewHolder {


        private ViewHolder() {
            super(SimpleItemView.this);
        }


        public SimpleItemView getView() {
            return SimpleItemView.this;
        }


    }


    public interface OnClickListener {


        void onClick(final SimpleItemView v);


    }


}
