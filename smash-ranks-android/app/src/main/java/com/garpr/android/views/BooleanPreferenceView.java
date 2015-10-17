package com.garpr.android.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.garpr.android.R;
import com.garpr.android.settings.BooleanSetting;

import butterknife.Bind;


public class BooleanPreferenceView extends PreferenceView implements Checkable {


    @Bind(R.id.view_boolean_preference_checkable)
    Checkable mCheckable;

    private BooleanSetting mSetting;
    private int mSubTitleEnabledText;
    private int mSubTitleDisabledText;
    private OnToggleListener mToggleListener;




    public BooleanPreferenceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    public BooleanPreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BooleanPreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public BooleanSetting getSetting() {
        return mSetting;
    }


    @Override
    public boolean isChecked() {
        return mSetting.get();
    }


    public void set(final BooleanSetting setting, final int titleText,
            final int subTitleEnabledText, final int subTitleDisabledText,
            final OnToggleListener toggleListener) {
        mSetting = setting;
        setTitleText(titleText);
        mSubTitleDisabledText = subTitleDisabledText;
        mSubTitleEnabledText = subTitleEnabledText;

        if (isChecked()) {
            setSubTitleText(subTitleEnabledText);
        } else {
            setSubTitleText(subTitleDisabledText);
        }

        mToggleListener = toggleListener;
        if (mToggleListener == null) {
            setClickable(false);
        } else {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    toggle();
                }
            });
        }
    }


    @Override
    public void setChecked(final boolean checked) {
        if (checked == isChecked()) {
            return;
        }

        toggle();
    }


    public void setOnToggleListener(final OnToggleListener l) {
        mToggleListener = l;
    }


    @Override
    public void toggle() {
        final boolean newValue = mSetting.toggle();
        mCheckable.setChecked(newValue);

        if (newValue) {
            setSubTitleText(mSubTitleEnabledText);
        } else {
            setSubTitleText(mSubTitleDisabledText);
        }

        if (mToggleListener != null) {
            mToggleListener.onToggle(this);
        }
    }




    public interface OnToggleListener {


        void onToggle(final BooleanPreferenceView v);


    }


}
