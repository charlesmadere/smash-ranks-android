package com.garpr.android.fragments;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;

import com.garpr.android.R;
import com.garpr.android.misc.Utils;
import com.garpr.android.models.Region;
import com.garpr.android.settings.Settings;
import com.garpr.android.views.CheckableItemView;

import butterknife.Bind;
import butterknife.OnClick;


public class FloatingActionButtonRegionsFragment extends RegionsFragment {


    private static final String TAG = "FloatingActionButtonRegionsFragment";

    @Bind(R.id.fragment_regions_save)
    FloatingActionButton mSave;

    private SaveListener mListener;




    public static FloatingActionButtonRegionsFragment create() {
        return new FloatingActionButtonRegionsFragment();
    }


    @Override
    protected int getContentView() {
        return R.layout.fragment_floating_action_button_regions;
    }


    @Override
    public String getFragmentName() {
        return TAG;
    }


    private void measureRecyclerViewBottomOffset() {
        final RecyclerView recyclerView = getRecyclerView();
        recyclerView.setClipToPadding(false);

        final Resources res = getResources();
        final int floatingActionButtonSize = res.getDimensionPixelSize(
                R.dimen.floating_action_button_size);
        final int floatingActionButtonMargin = res.getDimensionPixelSize(
                R.dimen.floating_action_button_margin);

        final int bottom = floatingActionButtonSize + (floatingActionButtonMargin * 2);
        final int start = ViewCompat.getPaddingStart(recyclerView);
        final int end = ViewCompat.getPaddingEnd(recyclerView);
        final int top = recyclerView.getPaddingTop();

        ViewCompat.setPaddingRelative(recyclerView, start, top, end, bottom);
        recyclerView.requestLayout();
    }


    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Region region = Settings.Region.get();

        if (mSelectedRegion == null || region.equals(mSelectedRegion)) {
            mSelectedRegion = region;
            mSave.hide();
        } else {
            mSave.show();
        }
    }


    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        mListener = (SaveListener) context;
    }


    @Override
    public void onClick(final CheckableItemView v) {
        super.onClick(v);
        final Region region = Settings.Region.get();

        if (region.equals(mSelectedRegion)) {
            mSave.hide();
        } else {
            mSave.show();
        }
    }


    @Override
    protected void prepareViews() {
        super.prepareViews();

        Utils.onGlobalLayout(mFrame, new Runnable() {
            @Override
            public void run() {
                measureRecyclerViewBottomOffset();
            }
        });

        if (mSelectedRegion != null) {
            mSave.show();
        }

        mSave.show();
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onRegionSaved();
            }
        });
    }


    @Override
    protected void showError() {
        super.showError();
        mSave.hide();
    }




    public interface SaveListener {


        void onRegionSaved();


    }


}
