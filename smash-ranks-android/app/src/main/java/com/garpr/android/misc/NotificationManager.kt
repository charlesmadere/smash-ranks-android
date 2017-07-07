package com.garpr.android.misc

import android.content.Context
import android.support.v4.app.NotificationCompat

interface NotificationManager {

    fun cancelAll()

    fun show(builder: NotificationCompat.Builder)

    fun showRankingsUpdated(context: Context)

}
