package com.garpr.android.networking;

import android.support.annotation.Nullable;

import com.garpr.android.misc.Heartbeat;

public interface ApiListener<T> extends Heartbeat {

    void failure();

    void success(@Nullable final T object);

}
