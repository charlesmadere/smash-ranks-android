package com.garpr.android.misc

import com.garpr.android.data.models.RankingsBundle

interface RankingsNotificationsUtils {

    fun getNotificationInfo(pollStatus: PollStatus?, rankingsBundle: RankingsBundle?): NotificationInfo

    fun getPollStatus(): PollStatus

    enum class NotificationInfo {
        CANCEL, NO_CHANGE, SHOW
    }

    data class PollStatus constructor(
            val oldRankingsId: String?,
            val proceed: Boolean,
            val retry: Boolean
    )

}
