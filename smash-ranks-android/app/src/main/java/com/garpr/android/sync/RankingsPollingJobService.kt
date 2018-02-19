package com.garpr.android.sync

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.garpr.android.App
import com.garpr.android.misc.NotificationsManager
import com.garpr.android.misc.RankingsNotificationsUtils
import com.garpr.android.misc.RankingsNotificationsUtils.PollStatus
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.Timber
import com.garpr.android.models.RankingsBundle
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import javax.inject.Inject

class RankingsPollingJobService : JobService(), ApiListener<RankingsBundle> {

    private var _isAlive: Boolean = true
    private var jobParameters: JobParameters? = null
    private var pollStatus: PollStatus? = null

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
        private const val TAG = "RankingsPollingJobService"
    }

    override fun failure(errorCode: Int) {
        timber.e(TAG, "failure fetching rankings ($errorCode)")
        notificationsManager.cancelAll()

        val pollStatus = this.pollStatus

        if (pollStatus != null) {
            this.pollStatus = pollStatus.copy(oldRankingsDate = pollStatus.oldRankingsDate,
                    proceed = pollStatus.proceed, retry = true)
        }

        jobFinished(true)
    }

    override val isAlive: Boolean
        get() = _isAlive

    private fun jobFinished(needsReschedule: Boolean) {
        jobParameters?.let {
            jobFinished(it, needsReschedule)
        }
    }

    override fun onCreate() {
        super.onCreate()
        App.get().appComponent.inject(this)
    }

    override fun onStartJob(job: JobParameters): Boolean {
        jobParameters = job

        val pollStatus = rankingsNotificationsUtils.getPollStatus()
        this.pollStatus = pollStatus

        return if (pollStatus.proceed) {
            serverApi.getRankings(regionManager.getRegion(this), this)
            true
        } else {
            false
        }
    }

    override fun onStopJob(job: JobParameters): Boolean {
        _isAlive = false
        return pollStatus?.retry == true
    }

    override fun success(`object`: RankingsBundle?) {
        val info = rankingsNotificationsUtils.getNotificationInfo(pollStatus, `object`)

        when (info) {
            RankingsNotificationsUtils.NotificationInfo.CANCEL -> {
                notificationsManager.cancelAll()
            }

            RankingsNotificationsUtils.NotificationInfo.NO_CHANGE -> {
                timber.d(TAG, "not changing any notifications")
            }

            RankingsNotificationsUtils.NotificationInfo.SHOW -> {
                notificationsManager.rankingsUpdated()
            }
        }

        jobFinished(false)
    }

}
