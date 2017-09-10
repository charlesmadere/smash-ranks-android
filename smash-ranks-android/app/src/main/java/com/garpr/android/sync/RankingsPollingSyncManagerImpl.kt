package com.garpr.android.sync

import com.firebase.jobdispatcher.Constraint
import com.firebase.jobdispatcher.Lifetime
import com.firebase.jobdispatcher.RetryStrategy
import com.firebase.jobdispatcher.Trigger
import com.garpr.android.misc.FirebaseApiWrapper
import com.garpr.android.misc.GoogleApiWrapper
import com.garpr.android.misc.Timber
import com.garpr.android.models.PollFrequency
import com.garpr.android.preferences.RankingsPollingPreferenceStore

class RankingsPollingSyncManagerImpl(
        private val mFirebaseApiWrapper: FirebaseApiWrapper,
        private val mGoogleApiWrapper: GoogleApiWrapper,
        private val mRankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val mTimber: Timber
) : RankingsPollingSyncManager {

    companion object {
        private const val TAG = "RankingsPollingSyncManagerImpl"
    }

    private fun disable() {
        mFirebaseApiWrapper.getJobDispatcher().cancel(TAG)
        mTimber.d(TAG, "sync has been disabled")
    }

    private fun enable() {
        if (!mGoogleApiWrapper.isGooglePlayServicesAvailable) {
            mTimber.w(TAG, "failed to schedule sync because Google Play Services are unavailable")
            return
        }

        val jobDispatcher = mFirebaseApiWrapper.getJobDispatcher()

        val jobBuilder = jobDispatcher.newJobBuilder()
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setService(RankingsPollingJobService::class.java)
                .setTag(TAG)

        if (mRankingsPollingPreferenceStore.chargingRequired.get() == true) {
            jobBuilder.addConstraint(Constraint.DEVICE_CHARGING)
        }

        if (mRankingsPollingPreferenceStore.wifiRequired.get() == true) {
            jobBuilder.addConstraint(Constraint.ON_UNMETERED_NETWORK)
        } else {
            jobBuilder.addConstraint(Constraint.ON_ANY_NETWORK)
        }

        val pollFrequency = mRankingsPollingPreferenceStore.pollFrequency.get() ?: PollFrequency.DAILY
        jobBuilder.trigger = Trigger.executionWindow(0, pollFrequency.timeInSeconds.toInt())
        jobDispatcher.mustSchedule(jobBuilder.build())

        mTimber.d(TAG, "sync has been enabled")
    }

    override fun enableOrDisable() {
        if (mRankingsPollingPreferenceStore.enabled.get() == true) {
            enable()
        } else {
            disable()
        }
    }

}
