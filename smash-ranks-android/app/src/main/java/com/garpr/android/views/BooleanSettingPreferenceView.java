package com.garpr.android.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.garpr.android.settings.BooleanSetting;


public class BooleanSettingPreferenceView extends PreferenceView implements Checkable {


    private BooleanSetting mSetting;
    private int mSubTitleEnabledText;
    private int mSubTitleDisabledText;
    private OnToggleListener mToggleListener;




    public BooleanSettingPreferenceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    public BooleanSetting getSetting() {
        return mSetting;
    }


    @Override
    public boolean isChecked() {
        return mSetting.get();
    }


    public void set(final BooleanSetting setting, final int titleText,
            final int subTitleEnabledText, final int subTitleDisabledText) {
        mSetting = setting;
        setTitleText(titleText);
        mSubTitleDisabledText = subTitleDisabledText;
        mSubTitleEnabledText = subTitleEnabledText;

        if (isChecked()) {
            setSubTitleText(subTitleEnabledText);
        } else {
            setSubTitleText(subTitleDisabledText);
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                toggle();
            }
        });
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
        if (mSetting.toggle()) {
            setSubTitleText(mSubTitleEnabledText);
        } else {
            setSubTitleText(mSubTitleDisabledText);
        }

        if (mToggleListener != null) {
            mToggleListener.onToggle(this);
        }
    }




    public interface OnToggleListener {


        void onToggle(final BooleanSettingPreferenceView v);


    }


}
