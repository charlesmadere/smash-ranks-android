package com.garpr.android.sync

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.garpr.android.App
import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.NotificationsManager
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.Timber
import com.garpr.android.models.RankingsBundle
import com.garpr.android.models.SimpleDate
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import javax.inject.Inject

class RankingsPollingJobService : JobService(), ApiListener<RankingsBundle> {

    private var mIsAlive: Boolean = true
    private var mRetry: Boolean = false
    private var mOldRankingsDate: SimpleDate? = null

    @Inject
    lateinit protected var mDeviceUtils: DeviceUtils

    @Inject
    lateinit protected var mNotificationsManager: NotificationsManager

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
        mTimber.e(TAG, "canceling any notifications, failure fetching rankings")
        mNotificationsManager.cancelAll()
        mRetry = true
    }

    override val isAlive: Boolean
        get() = mIsAlive

    override fun onStartJob(job: JobParameters?): Boolean {
        if (mRankingsPollingPreferenceStore.enabled.get() != true) {
            mTimber.e(TAG, "canceling sync, the feature is not enabled!")
            return false
        }

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

        mServerApi.getRankings(mRegionManager.getRegion(), this)
        return true
    }

    override fun onStopJob(job: JobParameters?): Boolean {
        mIsAlive = false
        return mRetry
    }

    override fun success(`object`: RankingsBundle?) {
        if (`object` == null) {
            mTimber.d(TAG, "canceling any notifications, received a null rankings bundle")
            mNotificationsManager.cancelAll()
            return
        }

        val newRankingsDate = mRankingsPollingPreferenceStore.rankingsDate.get()
        val oldRankingsDate = mOldRankingsDate

        if (newRankingsDate == null) {
            // should be impossible
            mTimber.e(TAG, "new rankings date is null! canceling any notifications")
            mNotificationsManager.cancelAll()
        } else if (oldRankingsDate == null) {
            // should be impossible
            mTimber.e(TAG, "old rankings date is null! canceling any notifications")
            mNotificationsManager.cancelAll()
        } else if (newRankingsDate.happenedAfter(oldRankingsDate)) {
            mNotificationsManager.rankingsUpdated(this)
        }
    }

}
