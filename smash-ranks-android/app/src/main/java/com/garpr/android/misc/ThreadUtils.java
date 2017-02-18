package com.garpr.android.misc;

import android.support.annotation.NonNull;

public interface ThreadUtils {

    void run(@NonNull final Task task);

    void runOnBackground(@NonNull final Runnable task);

    void runOnUi(@NonNull final Runnable task);


    interface Task {

        void onBackground();

        void onUi();

    }

}
