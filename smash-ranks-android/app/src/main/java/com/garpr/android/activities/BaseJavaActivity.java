package com.garpr.android.activities;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseJavaActivity extends BaseActivity {

    private Unbinder mUnbinder;


    @Override
    final void findViews() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }

        mUnbinder = ButterKnife.bind(this);
    }

}
