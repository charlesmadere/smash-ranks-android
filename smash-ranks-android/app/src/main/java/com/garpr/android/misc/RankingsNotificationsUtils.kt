package com.garpr.android.misc

import com.garpr.android.models.RankingsBundle
import com.garpr.android.models.SimpleDate

interface RankingsNotificationsUtils {

    fun getNotificationInfo(rankingsBundle: RankingsBundle?, newRankingsDate: SimpleDate?,
            oldRankingsDate: SimpleDate?): Info

    enum class Info {
        CANCEL, NO_CHANGE, SHOW
    }

}
