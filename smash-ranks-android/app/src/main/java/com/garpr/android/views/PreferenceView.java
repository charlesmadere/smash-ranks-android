package com.garpr.android.views;


import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garpr.android.R;


public class PreferenceView extends LinearLayout {


    private TextView mSubTitle;
    private TextView mTitle;




    public PreferenceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void dispatchRestoreInstanceState(@NonNull final SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }


    @Override
    protected void dispatchSaveInstanceState(@NonNull final SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mSubTitle = (TextView) findViewById(R.id.view_preference_sub_title);
        mTitle = (TextView) findViewById(R.id.view_preference_title);
    }


    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);

        if (enabled) {
            setAlpha(1f);
        } else {
            setAlpha(0.5f);
        }
    }


    public void setSubTitleText(final CharSequence text) {
        mSubTitle.setText(text);
    }


    public void setSubTitleText(final int resId) {
        mSubTitle.setText(resId);
    }


    public void setTitleText(final CharSequence text) {
        mTitle.setText(text);
    }


    public void setTitleText(final int resId) {
        mTitle.setText(resId);
    }


}
