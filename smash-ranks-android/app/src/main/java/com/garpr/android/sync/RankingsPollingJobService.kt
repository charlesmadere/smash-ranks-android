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
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import javax.inject.Inject

class RankingsPollingJobService : JobService(), ApiListener<RankingsBundle> {

    private var mIsAlive: Boolean = true
    private var mPollStatus: PollStatus? = null

    @Inject
    lateinit protected var mNotificationsManager: NotificationsManager

    @Inject
    lateinit protected var mRankingsNotificationsUtils: RankingsNotificationsUtils

    @Inject
    lateinit protected var mRankingsPollingPreferenceStore: RankingsPollingPreferenceStore

    @Inject
    lateinit protected var mRegionManager: RegionManager

    @Inject
    lateinit protected var mServerApi: ServerApi

    @Inject
    lateinit protected var mTimber: Timber


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
            mPollStatus = pollStatus.copy(oldDate = pollStatus.oldDate,
                    newDate = pollStatus.newDate, proceed = pollStatus.proceed, retry = true)
        }
    }

    override val isAlive: Boolean
        get() = mIsAlive

    override fun onStartJob(job: JobParameters?): Boolean {
        val pollStatus = mRankingsNotificationsUtils.getPollStatus()
        mPollStatus = pollStatus

        return if (pollStatus.proceed) {
            mServerApi.getRankings(mRegionManager.getRegion(this), this)
            true
        } else {
            false
        }
    }

    override fun onStopJob(job: JobParameters?): Boolean {
        mIsAlive = false
        return mPollStatus?.retry == true
    }

    override fun success(`object`: RankingsBundle?) {
        val info = mRankingsNotificationsUtils.getNotificationInfo(mPollStatus, `object`)

        when (info) {
            RankingsNotificationsUtils.Info.CANCEL -> {
                mNotificationsManager.cancelAll()
            }

            RankingsNotificationsUtils.Info.NO_CHANGE -> {
                mTimber.d(TAG, "not changing any notifications")
            }

            RankingsNotificationsUtils.Info.SHOW -> {
                mNotificationsManager.rankingsUpdated()
            }
        }
    }

}
