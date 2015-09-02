package com.garpr.android.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.garpr.android.R;


public abstract class BaseFragmentActivity extends BaseToolbarActivity {


    private Fragment mFragment;




    protected abstract Fragment createFragment();


    @Override
    protected int getContentView() {
        return R.layout.activity_base_fragment;
    }


    protected int getFragmentViewId() {
        return R.id.activity_base_fragment_content;
    }


    protected Fragment getFragment() {
        return mFragment;
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareFragment();
    }


    protected void prepareFragment() {
        final FragmentManager fManager = getSupportFragmentManager();
        final int fragmentViewId = getFragmentViewId();
        mFragment = fManager.findFragmentById(fragmentViewId);

        if (mFragment == null) {
            mFragment = createFragment();

            final FragmentTransaction fTransaction = fManager.beginTransaction();
            fTransaction.add(fragmentViewId, mFragment);
            fTransaction.commit();
        }
    }


}
