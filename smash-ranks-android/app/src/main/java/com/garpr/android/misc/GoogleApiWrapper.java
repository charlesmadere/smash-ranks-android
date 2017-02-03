package com.garpr.android.misc;

import android.app.Activity;
import android.content.DialogInterface.OnCancelListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface GoogleApiWrapper {

    int getGooglePlayServicesConnectionStatus();

    boolean isConnectionStatusSuccess(final int connectionStatus);

    boolean isGooglePlayServicesAvailable();

    boolean showPlayServicesResolutionDialog(final int connectionStatus,
            @NonNull final Activity activity, @Nullable final OnCancelListener onCancelListener);

}
