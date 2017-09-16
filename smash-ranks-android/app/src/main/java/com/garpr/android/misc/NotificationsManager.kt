package com.garpr.android.misc

import android.app.Notification
import android.support.v4.app.NotificationCompat

interface NotificationsManager {

    fun cancelAll()

    fun rankingsUpdated()

    fun show(id: Int, notificationBuilder: NotificationCompat.Builder)

    fun show(id: Int, notification: Notification)

}
