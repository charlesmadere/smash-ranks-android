package com.garpr.android.networking;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

public class ApiCall<T> implements ApiListener<T> {

    private final WeakReference<ApiListener<T>> mReference;


    public ApiCall(@NonNull final ApiListener<T> listener) {
        mReference = new WeakReference<>(listener);
    }

    @Override
    public void failure() {
        final ApiListener<T> listener = mReference.get();

        if (listener != null && listener.isAlive()) {
            listener.failure();
        }
    }

    @Override
    public boolean isAlive() {
        final ApiListener<T> listener = mReference.get();
        return listener != null && listener.isAlive();
    }

    @Override
    public void success(@Nullable final T object) {
        final ApiListener<T> listener = mReference.get();

        if (listener != null && listener.isAlive()) {
            listener.success(object);
        }
    }

}
