package com.garpr.android.features.sync.rankings

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.extensions.appComponent
import com.garpr.android.features.notifications.NotificationsManager
import com.garpr.android.misc.RankingsNotificationsUtils
import com.garpr.android.misc.Timber
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.repositories.RegionRepository
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
    protected lateinit var regionRepository: RegionRepository

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
                Result.retry()
            } else {
                timber.d(TAG, "won't proceed with work, won't retry")
                Result.success()
            }
        }

        var info: RankingsNotificationsUtils.NotificationInfo? = null

        serverApi.getRankings(regionRepository.getRegion(), object : ApiListener<RankingsBundle> {
            override fun failure(errorCode: Int) {
                timber.e(TAG, "error when fetching rankings ($errorCode)")
                info = null
            }

            override val isAlive: Boolean = true

            override fun success(`object`: RankingsBundle?) {
                timber.d(TAG, "successfully fetched rankings")
                info = rankingsNotificationsUtils.getNotificationInfo(pollStatus, `object`)
            }
        })

        timber.d(TAG, "work complete")

        return when (info) {
            RankingsNotificationsUtils.NotificationInfo.CANCEL -> {
                timber.d(TAG, "canceling notifications ($info)")
                notificationsManager.cancelAll()
                Result.success()
            }

            RankingsNotificationsUtils.NotificationInfo.NO_CHANGE -> {
                timber.d(TAG, "not changing any notifications ($info)")
                Result.success()
            }

            RankingsNotificationsUtils.NotificationInfo.SHOW -> {
                timber.d(TAG, "showing rankings updated notification ($info)")
                notificationsManager.rankingsUpdated()
                Result.success()
            }

            else -> {
                timber.e(TAG, "NotificationInfo is unknown ($info), not changing any notifications")
                Result.retry()
            }
        }
    }

}
