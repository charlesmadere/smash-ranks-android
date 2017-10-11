package com.garpr.android.misc

import com.garpr.android.models.RankingsBundle
import com.garpr.android.models.SimpleDate

interface RankingsNotificationsUtils {

    fun getNotificationInfo(pollStatus: PollStatus?, rankingsBundle: RankingsBundle?): NotificationInfo

    fun getPollStatus(): PollStatus

    enum class NotificationInfo {
        CANCEL, NO_CHANGE, SHOW
    }

    data class PollStatus constructor(
            val oldRankingsDate: SimpleDate?,
            val proceed: Boolean,
            val retry: Boolean
    )

}
