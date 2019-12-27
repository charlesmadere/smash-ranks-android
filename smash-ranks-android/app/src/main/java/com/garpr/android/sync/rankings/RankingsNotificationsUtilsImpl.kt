package com.garpr.android.sync.rankings

import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import com.garpr.android.sync.rankings.RankingsNotificationsUtils.NotificationInfo
import com.garpr.android.sync.rankings.RankingsNotificationsUtils.PollStatus

class RankingsNotificationsUtilsImpl(
        private val rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val timber: Timber
) : RankingsNotificationsUtils {

    companion object {
        private const val TAG = "RankingsNotificationsUtilsImpl"
    }

    override fun getNotificationInfo(pollStatus: PollStatus?, rankingsBundle: RankingsBundle?): NotificationInfo {
        return if (pollStatus == null || pollStatus.oldRankingsId.isNullOrBlank() || rankingsBundle == null) {
            NotificationInfo.CANCEL
        } else if (!rankingsBundle.id.contentEquals(pollStatus.oldRankingsId)) {
            NotificationInfo.SHOW
        } else {
            NotificationInfo.NO_CHANGE
        }
    }

    override fun getPollStatus(): PollStatus {
        val oldRankingsId = rankingsPollingPreferenceStore.rankingsId.get()

        if (rankingsPollingPreferenceStore.enabled.get() != true) {
            timber.e(TAG, "will not sync, polling is not enabled")
            return PollStatus(proceed = false, oldRankingsId = oldRankingsId)
        } else if (oldRankingsId == null) {
            timber.d(TAG, "will not sync, the user does not have a rankings ID")
            return PollStatus(proceed = false, oldRankingsId = oldRankingsId)
        }

        val lastPoll = rankingsPollingPreferenceStore.lastPoll.get()
        val currentPoll = SimpleDate()
        rankingsPollingPreferenceStore.lastPoll.set(currentPoll)
        timber.d(TAG, "will sync, last poll: $lastPoll, current poll: $currentPoll")
        return PollStatus(proceed = true, oldRankingsId = oldRankingsId)
    }

}
