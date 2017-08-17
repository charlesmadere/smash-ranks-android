package com.garpr.android.misc

import android.app.Activity
import android.content.DialogInterface.OnCancelListener

interface GoogleApiWrapper {

    val googlePlayServicesConnectionStatus: Int

    fun isConnectionStatusSuccess(connectionStatus: Int): Boolean

    val isGooglePlayServicesAvailable: Boolean

    fun showPlayServicesResolutionDialog(connectionStatus: Int,
            activity: Activity, onCancelListener: OnCancelListener?): Boolean

}
