package com.garpr.android.misc

import com.garpr.android.models.RankingsBundle
import com.garpr.android.models.SimpleDate

interface RankingsNotificationsUtils {

    fun getNotificationInfo(pollStatus: PollStatus?, rankingsBundle: RankingsBundle?): Info

    fun getPollStatus(): PollStatus

    enum class Info {
        CANCEL, NO_CHANGE, SHOW
    }

    data class PollStatus constructor(
            val oldDate: SimpleDate?,
            val newDate: SimpleDate?,
            val proceed: Boolean,
            val retry: Boolean
    )

}
