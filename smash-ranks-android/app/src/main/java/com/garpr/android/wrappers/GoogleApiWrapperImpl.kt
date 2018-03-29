package com.garpr.android.wrappers

import android.app.Activity
import android.app.Application
import android.content.DialogInterface.OnCancelListener
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

import com.garpr.android.R
import com.garpr.android.misc.CrashlyticsWrapper
import com.garpr.android.misc.Timber
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

class GoogleApiWrapperImpl(
        private val application: Application,
        private val crashlyticsWrapper: CrashlyticsWrapper,
        private val timber: Timber
) : GoogleApiWrapper {

    companion object {
        private const val TAG = "GoogleApiWrapperImpl"
        private const val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    }

    init {
        checkPlayServicesInstallationInfo()
    }

    private fun checkPlayServicesInstallationInfo() {
        val libraryVersion = application.resources.getInteger(R.integer.google_play_services_version)
        crashlyticsWrapper.setInt("gms_library_version", libraryVersion)

        var pi: PackageInfo? = null

        try {
            pi = application.packageManager.getPackageInfo("com.google.android.gms", 0)
        } catch (e: PackageManager.NameNotFoundException) {
            timber.w(TAG, "Error occurred when retrieving Google Play Services package info", e)
        }

        val versionCode = if (pi == null) "n/a" else pi.versionCode.toString()
        crashlyticsWrapper.setString("gms_version_code", versionCode)

        val versionName = if (pi == null) "n/a" else pi.versionName
        crashlyticsWrapper.setString("gms_version_name", versionName)
    }

    override val googlePlayServicesConnectionStatus: Int
        get() {
            val gaa = GoogleApiAvailability.getInstance()
            val connectionStatus = gaa.isGooglePlayServicesAvailable(application)
            val errorString = gaa.getErrorString(connectionStatus)
            timber.d(TAG, "Google Api Availability: $connectionStatus ($errorString)")

            crashlyticsWrapper.setInt("gms_connection_status", connectionStatus)
            crashlyticsWrapper.setString("gms_error_string", errorString)

            checkPlayServicesInstallationInfo()

            return connectionStatus
        }

    override fun isConnectionStatusSuccess(connectionStatus: Int): Boolean {
        return connectionStatus == ConnectionResult.SUCCESS
    }

    override val isGooglePlayServicesAvailable: Boolean
        get() = isConnectionStatusSuccess(googlePlayServicesConnectionStatus)

    override fun showPlayServicesResolutionDialog(activity: Activity, connectionStatus: Int,
            onCancelListener: OnCancelListener?): Boolean {
        val gaa = GoogleApiAvailability.getInstance()
        val errorString = gaa.getErrorString(connectionStatus)

        if (!gaa.isUserResolvableError(connectionStatus)) {
            timber.w(TAG, "Play Services error is not user resolvable, not showing" +
                    " resolution dialog ($connectionStatus): $errorString")
            return false
        } else if (activity.isFinishing) {
            timber.w(TAG, "activity is finishing, not showing Play Services " +
                    "resolution dialog ($connectionStatus): $errorString")
            return false
        } else if (activity.isDestroyed) {
            timber.w(TAG, "activity is destroyed, not showing Play Services " +
                    "resolution dialog ($connectionStatus): $errorString")
            return false
        }

        timber.d(TAG, "Showing Play Services resolution dialog ($connectionStatus): $errorString")
        gaa.showErrorDialogFragment(activity, connectionStatus, PLAY_SERVICES_RESOLUTION_REQUEST,
                onCancelListener)

        return true
    }

}
