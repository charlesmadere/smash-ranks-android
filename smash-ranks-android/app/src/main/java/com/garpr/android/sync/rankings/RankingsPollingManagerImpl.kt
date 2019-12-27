package com.garpr.android.sync.rankings

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import com.garpr.android.data.models.PollFrequency
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import com.garpr.android.wrappers.WorkManagerWrapper
import java.util.concurrent.TimeUnit
import java.net.URI as JavaUri

class RankingsPollingManagerImpl(
        private val rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val timber: Timber,
        private val workManagerWrapper: WorkManagerWrapper
) : RankingsPollingManager {

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

    override var isVibrationEnabled: Boolean
        get() = rankingsPollingPreferenceStore.vibrationEnabled.get() == true
        set(value) = rankingsPollingPreferenceStore.vibrationEnabled.set(value)

    override var isWifiRequired: Boolean
        get() = rankingsPollingPreferenceStore.wifiRequired.get() == true
        set(value) {
            rankingsPollingPreferenceStore.wifiRequired.set(value)
            enableOrDisable()
        }

    override var ringtone: JavaUri?
        get() = rankingsPollingPreferenceStore.ringtone.get()
        set(value) {
            rankingsPollingPreferenceStore.ringtone.set(value)
        }

    override var pollFrequency: PollFrequency
        get() = rankingsPollingPreferenceStore.pollFrequency.get() ?: PollFrequency.DAILY
        set(value) {
            rankingsPollingPreferenceStore.pollFrequency.set(value)
            enableOrDisable()
        }

    companion object {
        private const val TAG = "RankingsPollingManagerImpl"
    }

    private fun disable() {
        timber.d(TAG, "disabling rankings polling...")
        workManagerWrapper.cancelAllWorkByTag(TAG)
        timber.d(TAG, "rankings polling has been disabled")
    }

    private fun enable() {
        timber.d(TAG, "enabling rankings polling...")

        val constraints = Constraints.Builder()
                .setRequiredNetworkType(if (isWifiRequired) {
                    NetworkType.UNMETERED
                } else {
                    NetworkType.CONNECTED
                })
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(isChargingRequired)
                .build()

        val periodicRequest = PeriodicWorkRequest.Builder(
                RankingsPollingWorker::class.java,
                pollFrequency.timeInSeconds,
                TimeUnit.SECONDS
        )
                .addTag(TAG)
                .setConstraints(constraints)
                .build()

        workManagerWrapper.enqueue(periodicRequest)
        timber.d(TAG, "rankings polling has been enabled")
    }

    override fun enableOrDisable() {
        if (isEnabled) {
            enable()
        } else {
            disable()
        }
    }

}
