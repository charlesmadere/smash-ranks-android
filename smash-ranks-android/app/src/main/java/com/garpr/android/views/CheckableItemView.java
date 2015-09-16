package com.garpr.android.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
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

import butterknife.Bind;
import butterknife.ButterKnife;


public class CheckableItemView extends FrameLayout implements Checkable {


    @Bind(R.id.view_checkable_item_container)
    LinearLayout mContainer;

    @Bind(R.id.view_checkable_item_radio)
    RadioButton mRadio;

    @Bind(R.id.view_checkable_item_text)
    TextView mText;

    private ViewHolder mViewHolder;




    public static CheckableItemView inflate(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (CheckableItemView) inflater.inflate(R.layout.view_checkable_item, parent, false);
    }


    public CheckableItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    public CheckableItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CheckableItemView(final Context context, final AttributeSet attrs,
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
    public boolean isChecked() {
        return mRadio.isChecked();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
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




    public class ViewHolder extends RecyclerView.ViewHolder {


        protected ViewHolder() {
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
