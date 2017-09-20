package com.garpr.android.misc

import com.garpr.android.misc.RankingsNotificationsUtils.Info
import com.garpr.android.models.RankingsBundle
import com.garpr.android.models.SimpleDate

class RankingsNotificationsUtilsImpl : RankingsNotificationsUtils {

    override fun getNotificationInfo(rankingsBundle: RankingsBundle?, newRankingsDate: SimpleDate?,
            oldRankingsDate: SimpleDate?) =
        if (rankingsBundle == null || newRankingsDate == null || oldRankingsDate == null) {
            Info.CANCEL
        } else if (newRankingsDate.happenedAfter(oldRankingsDate)) {
            Info.SHOW
        } else {
            Info.NO_CHANGE
        }

}
