package com.garpr.android.misc

import com.garpr.android.misc.RankingsNotificationsUtils.NotificationInfo
import com.garpr.android.misc.RankingsNotificationsUtils.PollStatus
import com.garpr.android.models.RankingsBundle
import com.garpr.android.models.SimpleDate
import com.garpr.android.preferences.RankingsPollingPreferenceStore

class RankingsNotificationsUtilsImpl(
        private val deviceUtils: DeviceUtils,
        private val rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val timber: Timber
) : RankingsNotificationsUtils {

    companion object {
        private const val TAG = "RankingsNotificationsUtilsImpl"
    }

    override fun getNotificationInfo(pollStatus: PollStatus?, rankingsBundle: RankingsBundle?) =
        if (pollStatus?.oldRankingsDate == null || rankingsBundle == null) {
            NotificationInfo.CANCEL
        } else if (rankingsBundle.time.happenedAfter(pollStatus.oldRankingsDate)) {
            NotificationInfo.SHOW
        } else {
            NotificationInfo.NO_CHANGE
        }

    override fun getPollStatus(): PollStatus {
        if (rankingsPollingPreferenceStore.enabled.get() != true) {
            timber.e(TAG, "will not sync, polling is not enabled")
            return PollStatus(null, false, false)
        }

        val oldRankingsDate = rankingsPollingPreferenceStore.rankingsDate.get()

        if (oldRankingsDate == null) {
            timber.d(TAG, "will not sync, the user does not have a rankings date")
            return PollStatus(null, false, false)
        }

        if (!deviceUtils.hasNetworkConnection) {
            timber.d(TAG, "will retry sync later, the device does not have a network connection")
            return PollStatus(oldRankingsDate, false, true)
        }

        val lastPoll = rankingsPollingPreferenceStore.lastPoll.get()
        val currentPoll = SimpleDate()
        rankingsPollingPreferenceStore.lastPoll.set(currentPoll)
        timber.d(TAG, "will sync, last poll: $lastPoll, current poll: $currentPoll")

        return PollStatus(oldRankingsDate, true, true)
    }

}
