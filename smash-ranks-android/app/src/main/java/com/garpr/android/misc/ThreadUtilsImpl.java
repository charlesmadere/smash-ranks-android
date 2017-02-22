package com.garpr.android.misc;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtilsImpl implements ThreadUtils {

    private final ExecutorService mExecutorService;
    private final Handler mMainHandler;


    public ThreadUtilsImpl(final boolean isLowRamDevice) {
        mExecutorService = Executors.newFixedThreadPool(isLowRamDevice ? 2 : 3);
        mMainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void run(@NonNull final Task task) {
        runOnBackground(new Runnable() {
            @Override
            public void run() {
                task.onBackground();

                runOnUi(new Runnable() {
                    @Override
                    public void run() {
                        task.onUi();
                    }
                });
            }
        });
    }

    @Override
    public void runOnBackground(@NonNull final Runnable task) {
        mExecutorService.submit(task);
    }

    @Override
    public void runOnUi(@NonNull final Runnable task) {
        mMainHandler.post(task);
    }

}
