package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.garpr.android.lifecycle.LifecycleView;

public class LifecycleFrameLayout extends FrameLayout implements LifecycleView {

    public LifecycleFrameLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public LifecycleFrameLayout(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LifecycleFrameLayout(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

}
