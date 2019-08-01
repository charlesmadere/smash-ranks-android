package com.garpr.android.sync.rankings

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.garpr.android.extensions.appComponent
import com.garpr.android.features.notifications.NotificationsManager
import com.garpr.android.misc.Timber
import com.garpr.android.networking.ServerApi2
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
    protected lateinit var serverApi: ServerApi2

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

        try {
            val rankingsBundle = serverApi
                    .getRankings(regionRepository.getRegion())
                    .blockingGet()

            timber.d(TAG, "successfully fetched rankings")
            info = rankingsNotificationsUtils.getNotificationInfo(pollStatus, rankingsBundle)
        } catch (e: RuntimeException) {
            timber.e(TAG, "error when fetching rankings", e)
        }

        timber.d(TAG, "work complete")

        return when (info) {
            RankingsNotificationsUtils.NotificationInfo.CANCEL -> {
                timber.d(TAG, "canceling notifications ($info)")
                notificationsManager.cancelRankingsUpdated()
                Result.success()
            }

            RankingsNotificationsUtils.NotificationInfo.NO_CHANGE -> {
                timber.d(TAG, "not changing any notifications ($info)")
                Result.success()
            }

            RankingsNotificationsUtils.NotificationInfo.SHOW -> {
                timber.d(TAG, "showing rankings updated notification ($info)")
                notificationsManager.showRankingsUpdated()
                Result.success()
            }

            else -> {
                timber.e(TAG, "NotificationInfo is unknown ($info), not changing any notifications")
                Result.retry()
            }
        }
    }

}
