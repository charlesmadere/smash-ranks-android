package com.garpr.android.sync.rankings

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.garpr.android.managers.NotificationsManager
import com.garpr.android.misc.Timber
import com.garpr.android.networking.ServerApi
import com.garpr.android.repositories.RegionRepository
import com.garpr.android.sync.rankings.RankingsNotificationsUtils.NotificationInfo
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
            timber.d(TAG, "won't proceed with work")
            Result.success()
        }

        var info: NotificationInfo? = null

        try {
            val rankingsBundle = serverApi.getRankings(regionRepository.getRegion())
                    .blockingGet()

            timber.d(TAG, "successfully polled rankings")
            info = rankingsNotificationsUtils.getNotificationInfo(pollStatus, rankingsBundle)
        } catch (e: RuntimeException) {
            timber.e(TAG, "Exception when polling rankings", e)
        }

        timber.d(TAG, "work complete")

        return when (info) {
            NotificationInfo.CANCEL -> {
                timber.d(TAG, "canceling rankings notification ($info)")
                notificationsManager.cancelRankingsUpdated()
                Result.success()
            }

            NotificationInfo.NO_CHANGE -> {
                timber.d(TAG, "not changing any rankings notification ($info)")
                Result.success()
            }

            NotificationInfo.SHOW -> {
                timber.d(TAG, "showing rankings notification ($info)")
                notificationsManager.showRankingsUpdated()
                Result.success()
            }

            else -> {
                timber.w(TAG, "NotificationInfo is unknown, not changing any rankings notification ($info)")
                Result.retry()
            }
        }
    }

}
