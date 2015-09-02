package com.garpr.android.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garpr.android.App;
import com.garpr.android.misc.HeartbeatWithUi;
import com.garpr.android.models.Region;
import com.garpr.android.settings.RegionSetting;
import com.garpr.android.settings.Settings;


public abstract class BaseFragment extends Fragment implements HeartbeatWithUi,
        RegionSetting.RegionListener {


    private boolean mIsAlive;
    private Listener mListener;




    public int getColorCompat(final int colorResId) {
        return ContextCompat.getColor(getContext(), colorResId);
    }


    protected abstract int getContentView();


    public abstract String getFragmentName();


    protected LayoutInflater getLayoutInflater() {
        return getActivity().getLayoutInflater();
    }


    @Override
    public boolean isAlive() {
        return mIsAlive;
    }


    protected boolean listenForRegionChanges() {
        return false;
    }


    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (listenForRegionChanges()) {
            Settings.Region.attachListener(this, this);
        }
    }


    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        mListener = (Listener) context;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(getContentView(), container, false);
        mIsAlive = true;
        return view;
    }


    @Override
    public void onDestroyView() {
        mIsAlive = false;
        App.cancelNetworkRequests(this);

        if (listenForRegionChanges()) {
            Settings.Region.detachListener(this);
        }

        super.onDestroyView();
    }


    @Override
    public void onRegionChanged(final Region region) {
        // this method intentionally left blank (children can override)
    }


    @Override
    public void runOnUi(final Runnable action) {
        if (isAlive()) {
            mListener.runOnUi(action);
        }
    }


    @Override
    public String toString() {
        return getFragmentName();
    }




    public interface Listener {


        void runOnUi(final Runnable action);


    }


}
