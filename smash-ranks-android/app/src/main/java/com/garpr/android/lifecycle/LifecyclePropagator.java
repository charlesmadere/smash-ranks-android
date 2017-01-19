package com.garpr.android.lifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

public interface LifecyclePropagator {

    void findStateAndQueryBack(@NonNull final LifecycleView view);
    void onCreate(@NonNull final Activity activity, @Nullable final Bundle savedInstanceState);
    void onCreate(@NonNull final ViewGroup viewGroup, @Nullable final Bundle savedInstanceState);
    void onDestroy(@NonNull final Activity activity);
    void onDestroy(@NonNull final ViewGroup viewGroup);

}
