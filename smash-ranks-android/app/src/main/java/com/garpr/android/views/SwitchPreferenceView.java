package com.garpr.android.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;

import com.garpr.android.R;
import com.garpr.android.settings.BooleanSetting;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SwitchPreferenceView extends BooleanSettingPreferenceView {


    @Bind(R.id.view_switch_preference_switch)
    SwitchCompat mSwitch;




    public SwitchPreferenceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    public SwitchPreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwitchPreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }


    @Override
    public void set(final BooleanSetting setting, final int titleText,
            final int subTitleEnabledText, final int subTitleDisabledText,
            final OnToggleListener toggleListener) {
        super.set(setting, titleText, subTitleEnabledText, subTitleDisabledText, toggleListener);
        mSwitch.setChecked(isChecked());
    }


    @Override
    public void toggle() {
        super.toggle();
        mSwitch.setChecked(isChecked());
    }


}
