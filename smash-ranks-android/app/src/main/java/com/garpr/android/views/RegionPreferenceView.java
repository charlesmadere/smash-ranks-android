package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.RegionManager;

import javax.inject.Inject;

public class RegionPreferenceView extends SimplePreferenceView implements
        RegionManager.OnRegionChangeListener, View.OnClickListener {

    @Inject
    RegionManager mRegionManager;


    public RegionPreferenceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public RegionPreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RegionPreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) {
            return;
        }

        mRegionManager.addListener(this);
        refresh();
    }

    @Override
    public void onClick(final View v) {
        // TODO
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mRegionManager.removeListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (!isInEditMode()) {
            App.get().getAppComponent().inject(this);
        }

        setOnClickListener(this);
        setTitleText(R.string.region);

        if (isInEditMode()) {
            return;
        }

        mRegionManager.addListener(this);
        refresh();
    }

    @Override
    public void onRegionChange(final RegionManager regionManager) {
        if (ViewCompat.isAttachedToWindow(this)) {
            refresh();
        }
    }

    @Override
    public void refresh() {
        super.refresh();

        setDescriptionText(mRegionManager.getRegion());
    }

}
