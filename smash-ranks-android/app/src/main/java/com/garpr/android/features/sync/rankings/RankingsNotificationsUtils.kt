package com.garpr.android.features.sync.rankings

import com.garpr.android.data.models.RankingsBundle

interface RankingsNotificationsUtils {

    fun getNotificationInfo(pollStatus: PollStatus?, rankingsBundle: RankingsBundle?): NotificationInfo

    fun getPollStatus(): PollStatus

    enum class NotificationInfo {
        CANCEL, NO_CHANGE, SHOW
    }

    data class PollStatus(
            val oldRankingsId: String? = null,
            val proceed: Boolean,
            val retry: Boolean
    )

}
