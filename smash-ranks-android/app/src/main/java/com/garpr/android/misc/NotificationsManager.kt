package com.garpr.android.misc

import android.app.Notification
import android.content.Context
import android.support.v4.app.NotificationCompat

interface NotificationsManager {

    fun cancelAll()

    fun rankingsUpdated(context: Context)

    fun show(id: Int, notificationBuilder: NotificationCompat.Builder)

    fun show(id: Int, notification: Notification)

}
