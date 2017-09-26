package com.garpr.android.misc

import com.garpr.android.misc.RankingsNotificationsUtils.Info
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
        if (pollStatus?.oldDate == null || pollStatus.newDate == null || rankingsBundle == null) {
            Info.CANCEL
        } else if (pollStatus.newDate.happenedAfter(pollStatus.oldDate)) {
            Info.SHOW
        } else {
            Info.NO_CHANGE
        }

    override fun getPollStatus(): PollStatus {
        if (rankingsPollingPreferenceStore.enabled.get() != true) {
            timber.e(TAG, "canceling sync, the feature is not enabled")
            return PollStatus(null, null, false, false)
        }

        val oldDate = rankingsPollingPreferenceStore.rankingsDate.get()

        if (oldDate == null) {
            timber.d(TAG, "canceling sync, the user does not yet have a rankings date")
            return PollStatus(null, null, false, false)
        }

        if (deviceUtils.hasNetworkConnection()) {
            timber.d(TAG, "retrying sync, the device does not have a network connection")
            return PollStatus(oldDate, null, false, true)
        }

        val lastPoll = rankingsPollingPreferenceStore.lastPoll.get()
        val currentPoll = SimpleDate()
        rankingsPollingPreferenceStore.lastPoll.set(currentPoll)
        timber.d(TAG, "syncing now... last poll: $lastPoll, current poll: $currentPoll")

        return PollStatus(oldDate, currentPoll, true, true)
    }

}
