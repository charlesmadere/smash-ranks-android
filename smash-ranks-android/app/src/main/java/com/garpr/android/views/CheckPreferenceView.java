package com.garpr.android.views;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.garpr.android.R;
import com.garpr.android.settings.BooleanSetting;


public class CheckPreferenceView extends BooleanSettingPreferenceView {


    private CheckBox mCheckBox;




    public CheckPreferenceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mCheckBox = (CheckBox) findViewById(R.id.view_check_preference_check);
    }


    @Override
    public void set(final BooleanSetting setting, final int titleText,
            final int subTitleEnabledText, final int subTitleDisabledText) {
        super.set(setting, titleText, subTitleEnabledText, subTitleDisabledText);
        mCheckBox.setChecked(isChecked());
    }


    @Override
    public void toggle() {
        super.toggle();
        mCheckBox.setChecked(isChecked());
    }


}
