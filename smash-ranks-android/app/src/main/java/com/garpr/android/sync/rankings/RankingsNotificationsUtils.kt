package com.garpr.android.sync.rankings

import com.garpr.android.data.models.RankingsBundle

interface RankingsNotificationsUtils {

    fun getNotificationInfo(pollStatus: PollStatus?, rankingsBundle: RankingsBundle?): NotificationInfo

    fun getPollStatus(): PollStatus

    enum class NotificationInfo {
        CANCEL, NO_CHANGE, SHOW
    }

    class PollStatus(
            val proceed: Boolean,
            val retry: Boolean,
            val oldRankingsId: String? = null
    )

}
