package com.garpr.android.managers

import android.app.Notification
import androidx.core.app.NotificationCompat

interface NotificationsManager {

    fun cancelAll()

    fun rankingsUpdated()

    fun show(id: Int, notificationBuilder: NotificationCompat.Builder)

    fun show(id: Int, notification: Notification)

}
