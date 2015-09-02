package com.garpr.android.views;


import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;

import com.garpr.android.R;
import com.garpr.android.settings.BooleanSetting;


public class SwitchPreferenceView extends BooleanSettingPreferenceView {


    private SwitchCompat mSwitch;




    public SwitchPreferenceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mSwitch = (SwitchCompat) findViewById(R.id.view_switch_preference_switch);
    }


    @Override
    public void set(final BooleanSetting setting, final int titleText,
            final int subTitleEnabledText, final int subTitleDisabledText) {
        super.set(setting, titleText, subTitleEnabledText, subTitleDisabledText);
        mSwitch.setChecked(isChecked());
    }


    @Override
    public void toggle() {
        super.toggle();
        mSwitch.setChecked(isChecked());
    }


}
