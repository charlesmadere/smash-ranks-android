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
        private val firebaseApiWrapper: FirebaseApiWrapper,
        private val googleApiWrapper: GoogleApiWrapper,
        private val rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val timber: Timber
) : RankingsPollingSyncManager {

    companion object {
        private const val TAG = "RankingsPollingSyncManagerImpl"
    }

    private fun disable() {
        firebaseApiWrapper.getJobDispatcher().cancel(TAG)
        timber.d(TAG, "sync has been disabled")
    }

    private fun enable() {
        if (!googleApiWrapper.isGooglePlayServicesAvailable) {
            timber.w(TAG, "failed to schedule sync because Google Play Services are unavailable")
            return
        }

        val jobDispatcher = firebaseApiWrapper.getJobDispatcher()

        val jobBuilder = jobDispatcher.newJobBuilder()
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setService(RankingsPollingJobService::class.java)
                .setTag(TAG)

        if (isChargingRequired) {
            jobBuilder.addConstraint(Constraint.DEVICE_CHARGING)
        }

        if (isWifiRequired) {
            jobBuilder.addConstraint(Constraint.ON_UNMETERED_NETWORK)
        } else {
            jobBuilder.addConstraint(Constraint.ON_ANY_NETWORK)
        }

        val pollFrequency = rankingsPollingPreferenceStore.pollFrequency.get() ?: PollFrequency.DAILY
        jobBuilder.trigger = Trigger.executionWindow(0, pollFrequency.timeInSeconds.toInt())
        jobDispatcher.mustSchedule(jobBuilder.build())

        timber.d(TAG, "sync has been enabled")
    }

    override fun enableOrDisable() {
        if (isEnabled) {
            enable()
        } else {
            disable()
        }
    }

    override var isChargingRequired: Boolean
        get() = rankingsPollingPreferenceStore.chargingRequired.get() == true
        set(value) {
            rankingsPollingPreferenceStore.chargingRequired.set(value)
            enableOrDisable()
        }

    override var isEnabled: Boolean
        get() = rankingsPollingPreferenceStore.enabled.get() == true
        set(value) {
            rankingsPollingPreferenceStore.enabled.set(value)
            enableOrDisable()
        }

    override var isWifiRequired: Boolean
        get() = rankingsPollingPreferenceStore.wifiRequired.get() == true
        set(value) {
            rankingsPollingPreferenceStore.wifiRequired.set(value)
            enableOrDisable()
        }

}
