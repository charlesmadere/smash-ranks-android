package com.garpr.android.misc;

import android.support.annotation.NonNull;

public interface CrashlyticsWrapper {

    void initialize(final boolean disabled);

    void log(final int priority, final String tag, final String msg);

    void logException(@NonNull final Throwable tr);

    void setBool(@NonNull final String key, final boolean value);

    void setInt(@NonNull final String key, final int value);

}
