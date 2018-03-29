package com.garpr.android.wrappers

import android.app.Activity
import android.content.DialogInterface.OnCancelListener

interface GoogleApiWrapper {

    val googlePlayServicesConnectionStatus: Int

    fun isConnectionStatusSuccess(connectionStatus: Int): Boolean

    val isGooglePlayServicesAvailable: Boolean

    fun showPlayServicesResolutionDialog(activity: Activity, connectionStatus: Int,
            onCancelListener: OnCancelListener?): Boolean

}
