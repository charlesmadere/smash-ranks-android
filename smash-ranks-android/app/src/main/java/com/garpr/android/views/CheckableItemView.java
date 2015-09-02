package com.garpr.android.views;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.garpr.android.R;


public class CheckableItemView extends FrameLayout implements Checkable {


    private LinearLayout mContainer;
    private RadioButton mRadio;
    private TextView mText;
    private ViewHolder mViewHolder;




    public static CheckableItemView inflate(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (CheckableItemView) inflater.inflate(R.layout.view_checkable_item, parent, false);
    }


    public CheckableItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    public ViewHolder getViewHolder() {
        if (mViewHolder == null) {
            mViewHolder = new ViewHolder();
        }

        return mViewHolder;
    }


    @Override
    public boolean isChecked() {
        return mRadio.isChecked();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContainer = (LinearLayout) findViewById(R.id.view_checkable_item_container);
        mRadio = (RadioButton) findViewById(R.id.view_checkable_item_radio);
        mText = (TextView) findViewById(R.id.view_checkable_item_text);
    }


    @Override
    public void setChecked(final boolean checked) {
        mRadio.setChecked(checked);
    }


    public void setOnClickListener(final OnClickListener l) {
        if (l == null) {
            mContainer.setClickable(false);
        } else {
            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    l.onClick(CheckableItemView.this);
                }
            });
        }
    }


    public void setText(final CharSequence text) {
        mText.setText(text);
    }


    @Override
    public void toggle() {
        setChecked(!isChecked());
    }




    public final class ViewHolder extends RecyclerView.ViewHolder {


        private ViewHolder() {
            super(CheckableItemView.this);
        }


        public CheckableItemView getView() {
            return CheckableItemView.this;
        }


    }


    public interface OnClickListener {


        void onClick(final CheckableItemView v);


    }


}
