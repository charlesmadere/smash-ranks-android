package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.activities.SetRegionActivity;
import com.garpr.android.misc.Heartbeat;
import com.garpr.android.misc.RegionManager;

import javax.inject.Inject;

public class RegionPreferenceView extends SimplePreferenceView implements Heartbeat,
        RegionManager.OnRegionChangeListener, View.OnClickListener {

    @Inject
    RegionManager mRegionManager;


    public RegionPreferenceView(@NonNull final Context context,
            @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public RegionPreferenceView(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RegionPreferenceView(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean isAlive() {
        return ViewCompat.isAttachedToWindow(this);
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
        final Context context = getContext();
        context.startActivity(new Intent(context, SetRegionActivity.class));
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
        setTitleText(R.string.set_your_region);

        if (isInEditMode()) {
            return;
        }

        mRegionManager.addListener(this);
        refresh();
    }

    @Override
    public void onRegionChange(final RegionManager regionManager) {
        if (isAlive()) {
            refresh();
        }
    }

    @Override
    public void refresh() {
        super.refresh();

        setDescriptionText(mRegionManager.getRegion().getDisplayName());
    }

}
