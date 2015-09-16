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

import butterknife.Bind;
import butterknife.ButterKnife;


public class SimpleSeparatorView extends FrameLayout {


    @Bind(R.id.view_simple_separator_item_text)
    TextView mText;

    private ViewHolder mViewHolder;




    public static SimpleSeparatorView inflate(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (SimpleSeparatorView) inflater.inflate(R.layout.view_simple_separator_item, parent,
                false);
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


    public void setOnClickListener(final OnClickListener l) {
        if (l == null) {
            setClickable(false);
        } else {
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    l.onClick(SimpleSeparatorView.this);
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




    public class ViewHolder extends RecyclerView.ViewHolder {


        protected ViewHolder() {
            super(SimpleSeparatorView.this);
        }


        public SimpleSeparatorView getView() {
            return SimpleSeparatorView.this;
        }


    }


    public interface OnClickListener {


        void onClick(final SimpleSeparatorView v);


    }


}
