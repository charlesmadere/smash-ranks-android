package com.garpr.android.sync.rankings

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.garpr.android.extensions.appComponent
import com.garpr.android.managers.NotificationsManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.RankingsNotificationsUtils
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.models.RankingsBundle
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import javax.inject.Inject

class RankingsPollingWorker(
        context: Context,
        workerParams: WorkerParameters
) : Worker(context, workerParams) {

    @Inject
    protected lateinit var notificationsManager: NotificationsManager

    @Inject
    protected lateinit var rankingsNotificationsUtils: RankingsNotificationsUtils

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var serverApi: ServerApi

    @Inject
    protected lateinit var timber: Timber


    companion object {
        private const val TAG = "RankingsPollingWorker"
    }

    init {
        context.appComponent.inject(this)
    }

    override fun doWork(): Result {
        timber.d(TAG, "work starting...")

        val pollStatus = rankingsNotificationsUtils.getPollStatus()

        if (!pollStatus.proceed) {
            return if (pollStatus.retry) {
                timber.d(TAG, "won't proceed with work, will retry")
                Result.RETRY
            } else {
                timber.d(TAG, "won't proceed with work, nothing to do")
                Result.SUCCESS
            }
        }

        var info: RankingsNotificationsUtils.NotificationInfo? = null

        serverApi.getRankings(regionManager.getRegion(), object : ApiListener<RankingsBundle> {
            override fun failure(errorCode: Int) {
                info = null
            }

            override val isAlive: Boolean = true

            override fun success(`object`: RankingsBundle?) {
                info = rankingsNotificationsUtils.getNotificationInfo(pollStatus, `object`)
            }
        })

        timber.d(TAG, "work complete")

        return when (info) {
            RankingsNotificationsUtils.NotificationInfo.CANCEL -> {
                notificationsManager.cancelAll()
                Result.SUCCESS
            }

            RankingsNotificationsUtils.NotificationInfo.NO_CHANGE -> {
                timber.d(TAG, "not changing any notifications")
                Result.SUCCESS
            }

            RankingsNotificationsUtils.NotificationInfo.SHOW -> {
                notificationsManager.rankingsUpdated()
                Result.SUCCESS
            }

            else -> {
                timber.e(TAG, "NotificationInfo is unknown ($info), not changing any notifications")
                Result.RETRY
            }
        }
    }

}
