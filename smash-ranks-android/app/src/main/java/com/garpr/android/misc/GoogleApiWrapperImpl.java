package com.garpr.android.misc;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class GoogleApiWrapperImpl implements GoogleApiWrapper {

    private static final String TAG = "GoogleApiWrapperImpl";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private final Application mApplication;
    private final CrashlyticsWrapper mCrashlyticsWrapper;
    private final Timber mTimber;


    public GoogleApiWrapperImpl(@NonNull final Application application,
            @NonNull final CrashlyticsWrapper crashlyticsWrapper, @NonNull final Timber timber) {
        mApplication = application;
        mCrashlyticsWrapper = crashlyticsWrapper;
        mTimber = timber;

        checkPlayServicesInstallationInfo();
    }

    private void checkPlayServicesInstallationInfo() {
        final int libraryVersion = mApplication.getResources().getInteger(R.integer.google_play_services_version);
        mCrashlyticsWrapper.setInt("gms_library_version", libraryVersion);

        PackageInfo pi = null;

        try {
            final PackageManager pm = mApplication.getPackageManager();
            pi = pm.getPackageInfo("com.google.android.gms", 0);
        } catch (final PackageManager.NameNotFoundException e) {
            mTimber.w(TAG, "Error occurred when retrieving Google Play Services package info", e);
        }

        final String versionCode = pi == null ? "n/a" : String.valueOf(pi.versionCode);
        mCrashlyticsWrapper.setString("gms_version_code", versionCode);

        final String versionName = pi == null ? "n/a" : pi.versionName;
        mCrashlyticsWrapper.setString("gms_version_name", versionName);
    }

    @Override
    public int getGooglePlayServicesConnectionStatus() {
        final GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        final int connectionStatus = gaa.isGooglePlayServicesAvailable(mApplication);
        final String errorString = gaa.getErrorString(connectionStatus);
        mTimber.d(TAG, "Google Api Availability: " + errorString + " (" + connectionStatus + ")");

        mCrashlyticsWrapper.setInt("gms_connection_status", connectionStatus);
        mCrashlyticsWrapper.setString("gms_error_string", errorString);

        checkPlayServicesInstallationInfo();

        return connectionStatus;
    }

    @Override
    public boolean isConnectionStatusSuccess(final int connectionStatus) {
        return connectionStatus == ConnectionResult.SUCCESS;
    }

    @Override
    public boolean isGooglePlayServicesAvailable() {
        return isConnectionStatusSuccess(getGooglePlayServicesConnectionStatus());
    }

    @Override
    public boolean showPlayServicesResolutionDialog(final int connectionStatus,
            @NonNull final Activity activity, @Nullable final OnCancelListener onCancelListener) {
        final GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        final String errorString = gaa.getErrorString(connectionStatus);

        if (!gaa.isUserResolvableError(connectionStatus)) {
            mTimber.w(TAG, "Play Services error is not user resolvable, not showing" +
                    " resolution dialog (" + connectionStatus + "): " + errorString);
            return false;
        } else if (activity.isFinishing()) {
            mTimber.w(TAG, "activity is finishing, not showing Play Services resolution dialog ("
                    + connectionStatus + "): " + errorString);
            return false;
        } else if (activity.isDestroyed()) {
            mTimber.w(TAG, "activity is destroyed, not showing Play Services resolution dialog ("
                    + connectionStatus + "): " + errorString);
            return false;
        }

        mTimber.d(TAG, "Showing Play Services resolution dialog (" +  connectionStatus +
                "): " + errorString);
        gaa.showErrorDialogFragment(activity, connectionStatus, PLAY_SERVICES_RESOLUTION_REQUEST,
                onCancelListener);

        return true;
    }

}
