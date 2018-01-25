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

    private var mIsAlive: Boolean = true
    private var mJobParameters: JobParameters? = null
    private var mPollStatus: PollStatus? = null

    @Inject
    protected lateinit var mNotificationsManager: NotificationsManager

    @Inject
    protected lateinit var mRankingsNotificationsUtils: RankingsNotificationsUtils

    @Inject
    protected lateinit var mRegionManager: RegionManager

    @Inject
    protected lateinit var mServerApi: ServerApi

    @Inject
    protected lateinit var mTimber: Timber


    companion object {
        private const val TAG = "RankingsPollingJobService"
    }

    init {
        App.get().appComponent.inject(this)
    }

    override fun failure(errorCode: Int) {
        mTimber.e(TAG, "failure fetching rankings ($errorCode)")
        mNotificationsManager.cancelAll()

        val pollStatus = mPollStatus

        if (pollStatus != null) {
            mPollStatus = pollStatus.copy(oldRankingsDate = pollStatus.oldRankingsDate,
                    proceed = pollStatus.proceed, retry = true)
        }

        jobFinished(true)
    }

    override val isAlive: Boolean
        get() = mIsAlive

    private fun jobFinished(needsReschedule: Boolean) {
        mJobParameters?.let {
            jobFinished(it, needsReschedule)
        }
    }

    override fun onStartJob(job: JobParameters): Boolean {
        mJobParameters = job

        val pollStatus = mRankingsNotificationsUtils.getPollStatus()
        mPollStatus = pollStatus

        return if (pollStatus.proceed) {
            mServerApi.getRankings(mRegionManager.getRegion(this), this)
            true
        } else {
            false
        }
    }

    override fun onStopJob(job: JobParameters): Boolean {
        mIsAlive = false
        return mPollStatus?.retry == true
    }

    override fun success(`object`: RankingsBundle?) {
        val info = mRankingsNotificationsUtils.getNotificationInfo(mPollStatus, `object`)

        when (info) {
            RankingsNotificationsUtils.NotificationInfo.CANCEL -> {
                mNotificationsManager.cancelAll()
            }

            RankingsNotificationsUtils.NotificationInfo.NO_CHANGE -> {
                mTimber.d(TAG, "not changing any notifications")
            }

            RankingsNotificationsUtils.NotificationInfo.SHOW -> {
                mNotificationsManager.rankingsUpdated()
            }
        }

        jobFinished(false)
    }

}
