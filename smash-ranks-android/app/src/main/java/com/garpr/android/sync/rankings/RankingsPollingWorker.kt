package com.garpr.android.sync.rankings

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.garpr.android.managers.NotificationsManager
import com.garpr.android.misc.Timber
import com.garpr.android.networking.ServerApi
import com.garpr.android.repositories.RegionRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class RankingsPollingWorker(
        context: Context,
        workerParams: WorkerParameters
) : Worker(context, workerParams), KoinComponent {

    protected val notificationsManager: NotificationsManager by inject()
    protected val rankingsNotificationsUtils: RankingsNotificationsUtils by inject()
    protected val regionRepository: RegionRepository by inject()
    protected val serverApi: ServerApi by inject()
    protected val timber: Timber by inject()

    companion object {
        private const val TAG = "RankingsPollingWorker"
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
