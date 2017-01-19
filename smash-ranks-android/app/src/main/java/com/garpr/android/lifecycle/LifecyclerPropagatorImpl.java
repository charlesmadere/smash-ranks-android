package com.garpr.android.lifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.garpr.android.misc.MiscUtils;

import static com.garpr.android.lifecycle.ActivityState.CREATED;
import static com.garpr.android.lifecycle.ActivityState.DESTROYED;
import static com.garpr.android.lifecycle.ActivityState.PAUSED;
import static com.garpr.android.lifecycle.ActivityState.RESUMED;
import static com.garpr.android.lifecycle.ActivityState.STARTED;
import static com.garpr.android.lifecycle.ActivityState.STOPPED;

public class LifecyclerPropagatorImpl implements LifecyclePropagator {

    @Override
    public void findStateAndQueryBack(@NonNull final LifecycleView view) {
        if (!(view instanceof View)) {
            throw new IllegalArgumentException();
        }

        final Activity activity = MiscUtils.getActivity(((View) view).getContext());

        if (!(activity instanceof ActivityStateHandle)) {
            throw new RuntimeException();
        }

        final ActivityStateHandle handle = ((ActivityStateHandle) activity);
        final ActivityState state = handle.getActivityState();

        if (state == null) {
            return;
        }

        if (state == CREATED || state == STARTED || state == RESUMED || state == PAUSED ||
                state == STOPPED || state == DESTROYED) {
            view.onCreate(handle.getSavedInstanceState());
        }

        if (state == STARTED || state == RESUMED || state == PAUSED || state == STOPPED ||
                state == DESTROYED) {
            view.onStart();
        }

        if (state == RESUMED || state == PAUSED || state == STOPPED || state == DESTROYED) {
            view.onResume();
        }

        if (state == PAUSED || state == STOPPED || state == DESTROYED) {
            view.onPause();
        }

        if (state == STOPPED || state == DESTROYED) {
            view.onStop();
        }

        if (state == DESTROYED) {
            view.onDestroy();
        }
    }

    private ViewGroup getActivityRootView(@NonNull final Activity activity) {
        return (ViewGroup) activity.findViewById(android.R.id.content);
    }

    @Override
    public void onCreate(@NonNull final Activity activity,
            @Nullable final Bundle savedInstanceState) {
        onCreate(getActivityRootView(activity), savedInstanceState);
    }

    @Override
    public void onCreate(@NonNull final ViewGroup viewGroup,
            @Nullable final Bundle savedInstanceState) {
        for (int i = 0; i < viewGroup.getChildCount(); ++i) {
            final View view = viewGroup.getChildAt(i);

            if (view instanceof LifecycleView) {
                ((LifecycleView) view).onCreate(savedInstanceState);
            } else if (view instanceof ViewGroup) {
                onDestroy((ViewGroup) view);
            }
        }
    }

    @Override
    public void onDestroy(@NonNull final Activity activity) {
        onDestroy(getActivityRootView(activity));
    }

    @Override
    public void onDestroy(@NonNull final ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); ++i) {
            final View view = viewGroup.getChildAt(i);

            if (view instanceof LifecycleView) {
                ((LifecycleView) view).onDestroy();
            } else if (view instanceof ViewGroup) {
                onDestroy((ViewGroup) view);
            }
        }
    }

}
