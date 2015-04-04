package com.garpr.android.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.garpr.android.R;


public class SimpleSeparatorView extends FrameLayout {


    private FrameLayout mContainer;
    private TextView mText;
    private ViewHolder mViewHolder;




    public SimpleSeparatorView(final Context context) {
        super(context);
    }


    public SimpleSeparatorView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    public SimpleSeparatorView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimpleSeparatorView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public FrameLayout getContainerView() {
        return mContainer;
    }


    public TextView getTextView() {
        return mText;
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
        mContainer = (FrameLayout) findViewById(R.id.view_simple_separator_item_container);
        mText = (TextView) findViewById(R.id.view_simple_separator_item_text);
    }


    public void setText(final CharSequence text) {
        mText.setText(text);
    }




    public final class ViewHolder extends RecyclerView.ViewHolder {


        private ViewHolder() {
            super(SimpleSeparatorView.this);
        }


        public SimpleSeparatorView getView() {
            return SimpleSeparatorView.this;
        }


    }


}
