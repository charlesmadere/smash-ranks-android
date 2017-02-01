package com.garpr.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.misc.Heartbeat;
import com.garpr.android.misc.Timber;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements Heartbeat {

    private static final String TAG = "BaseFragment";

    private boolean mIsAlive;
    private Unbinder mUnbinder;

    @Inject
    protected Timber mTimber;


    protected abstract String getFragmentName();

    @Override
    public boolean isAlive() {
        return mIsAlive && isAdded() && !isRemoving();
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);
        mTimber.d(TAG, getFragmentName() + " created");
    }

    @Override
    public void onDestroyView() {
        mIsAlive = false;

        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }

        super.onDestroyView();
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        mIsAlive = true;
    }

    @Override
    public String toString() {
        return getFragmentName();
    }

}
