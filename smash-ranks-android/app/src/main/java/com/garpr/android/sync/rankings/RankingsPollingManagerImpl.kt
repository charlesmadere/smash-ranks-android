package com.garpr.android.sync.rankings

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.garpr.android.misc.Timber
import com.garpr.android.models.PollFrequency
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import java.util.concurrent.TimeUnit

class RankingsPollingManagerImpl(
        private val rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val timber: Timber
) : RankingsPollingManager {

    companion object {
        private const val TAG = "RankingsPollingManagerImpl"
    }

    private fun disable() {
        timber.d(TAG, "disabling polling...")
        workManager.cancelAllWorkByTag(TAG)
        timber.d(TAG, "polling has been disabled")
    }

    private fun enable() {
        timber.d(TAG, "enabling polling...")

        val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(isChargingRequired)

        if (isWifiRequired) {
            constraints.setRequiredNetworkType(NetworkType.UNMETERED)
        } else {
            constraints.setRequiredNetworkType(NetworkType.CONNECTED)
        }

        val periodicRequest = PeriodicWorkRequest.Builder(
                RankingsPollingWorker::class.java,
                pollFrequency.timeInSeconds,
                TimeUnit.SECONDS
        )
        .addTag(TAG)
        .setConstraints(constraints.build())
        .build()

        workManager.enqueue(periodicRequest)
        timber.d(TAG, "polling has been enabled")
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

    override var pollFrequency: PollFrequency
        get() = rankingsPollingPreferenceStore.pollFrequency.get() ?: PollFrequency.DAILY
        set(value) {
            rankingsPollingPreferenceStore.pollFrequency.set(value)
            enableOrDisable()
        }

    private val workManager: WorkManager
        get() = WorkManager.getInstance()

}
