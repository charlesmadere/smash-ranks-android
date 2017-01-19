package com.garpr.android.lifecycle;

import android.os.Bundle;
import android.support.annotation.Nullable;

public interface LifecycleView {

    void onCreate(@Nullable final Bundle savedInstanceState);
    void onDestroy();
    void onPause();
    void onResume();
    void onSaveInstanceState(final Bundle outState);
    void onStart();
    void onStop();

}
