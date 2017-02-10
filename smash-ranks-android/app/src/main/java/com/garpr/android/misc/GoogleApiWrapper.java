package com.garpr.android.misc;

import android.app.Activity;
import android.content.DialogInterface.OnCancelListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.gcm.GcmNetworkManager;

public interface GoogleApiWrapper {

    GcmNetworkManager getGcmNetworkManager();

    int getGooglePlayServicesConnectionStatus();

    boolean isConnectionStatusSuccess(final int connectionStatus);

    boolean isGooglePlayServicesAvailable();

    boolean showPlayServicesResolutionDialog(final int connectionStatus,
            @NonNull final Activity activity, @Nullable final OnCancelListener onCancelListener);

}
