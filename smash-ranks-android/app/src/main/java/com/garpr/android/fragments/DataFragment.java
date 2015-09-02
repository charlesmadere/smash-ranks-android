package com.garpr.android.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.garpr.android.misc.Heartbeat;


public abstract class DataFragment extends Fragment implements Heartbeat {


    private boolean mIsAlive;




    protected static DataFragment create(final DataFragment fragment) {
        fragment.setRetainInstance(true);
        return fragment;
    }


    protected abstract String getFragmentName();


    @Override
    public boolean isAlive() {
        return mIsAlive;
    }


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsAlive = true;
    }


    @Override
    public void onDestroy() {
        mIsAlive = false;
        super.onDestroy();
    }


    @Override
    public String toString() {
        return getFragmentName();
    }


}
