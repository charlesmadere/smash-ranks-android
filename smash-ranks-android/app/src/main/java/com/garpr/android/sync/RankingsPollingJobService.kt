package com.garpr.android.sync

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.garpr.android.App
import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.NotificationManager
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.Timber
import com.garpr.android.models.RankingsBundle
import com.garpr.android.models.SimpleDate
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import javax.inject.Inject

class RankingsPollingJobService : JobService(), ApiListener<RankingsBundle> {

    @JvmField
    val TAG = "RankingsPollingJobService"

    private var mIsAlive: Boolean = true
    private var mRetry: Boolean = false
    private var mOldRankingsDate: SimpleDate? = null

    @Inject
    lateinit var mDeviceUtils: DeviceUtils

    @Inject
    lateinit var mNotificationManager: NotificationManager

    @Inject
    lateinit var mRankingsPollingPreferenceStore: RankingsPollingPreferenceStore

    @Inject
    lateinit var mRegionManager: RegionManager

    @Inject
    lateinit var mServerApi: ServerApi

    @Inject
    lateinit var mTimber: Timber


    init {
        App.get().appComponent.inject(this)
    }

    override fun failure(errorCode: Int) {
        mTimber.e(TAG, "canceling any notifications, failure fetching rankings")
        mNotificationManager.cancelAll()
        mRetry = true
    }

    override fun isAlive(): Boolean {
        return mIsAlive
    }

    override fun onStartJob(job: JobParameters?): Boolean {
        mOldRankingsDate = mRankingsPollingPreferenceStore.rankingsDate.get()

        if (mOldRankingsDate == null) {
            mTimber.d(TAG, "canceling sync, the user does not yet have a rankings date")
            return false
        }

        if (!mDeviceUtils.hasNetworkConnection()) {
            mTimber.d(TAG, "retrying sync, the device does not have a network connection")
            mRetry = true
            return false
        }

        val lastPoll = mRankingsPollingPreferenceStore.lastPoll.get()
        val currentPoll = SimpleDate()
        mRankingsPollingPreferenceStore.lastPoll.set(currentPoll)
        mTimber.d(TAG, "syncing now... last poll: $lastPoll current poll: $currentPoll")

        mServerApi.getRankings(mRegionManager.region, this)
        return true
    }

    override fun onStopJob(job: JobParameters?): Boolean {
        mIsAlive = false
        return mRetry
    }

    override fun success(`object`: RankingsBundle?) {
        if (`object` == null) {
            mTimber.d(TAG, "canceling any notifications, received a null rankings bundle")
            mNotificationManager.cancelAll()
            return
        }

        val newRankingsDate = mRankingsPollingPreferenceStore.rankingsDate.get()

        if (newRankingsDate == null) {
            // should be impossible
            mTimber.e(TAG, "new rankings date is null! caneling any notifications")
            mNotificationManager.cancelAll()
        } else if (newRankingsDate.happenedAfter(mOldRankingsDate!!)) {
            mNotificationManager.showRankingsUpdated(this)
        }
    }

}
