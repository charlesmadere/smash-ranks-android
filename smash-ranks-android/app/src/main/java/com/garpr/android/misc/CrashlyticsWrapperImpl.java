package com.garpr.android.misc;

import android.app.Application;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;

public class CrashlyticsWrapperImpl implements CrashlyticsWrapper {

    private final Application mApplication;


    public CrashlyticsWrapperImpl(final Application application) {
        mApplication = application;
    }

    @Override
    public void initialize(final boolean disabled) {
        final CrashlyticsCore crashlyticsCore = new CrashlyticsCore.Builder()
                .disabled(disabled)
                .build();

        final Crashlytics crashlytics = new Crashlytics.Builder()
                .core(crashlyticsCore)
                .build();

        Fabric.with(mApplication, crashlytics);
    }

    @Override
    public void log(final int priority, final String tag, final String msg) {
        Crashlytics.log(priority, tag, msg);
    }

    @Override
    public void logException(@NonNull final Throwable tr) {
        Crashlytics.logException(tr);
    }

    @Override
    public void setBool(@NonNull final String key, final boolean value) {
        Crashlytics.setBool(key, value);
    }

    @Override
    public void setInt(@NonNull final String key, final int value) {
        Crashlytics.setInt(key, value);
    }

}
