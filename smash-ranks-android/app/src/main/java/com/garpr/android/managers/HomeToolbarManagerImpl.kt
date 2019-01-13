package com.garpr.android.managers

import com.garpr.android.data.models.RankingCriteria
import com.garpr.android.managers.HomeToolbarManager.Presentation

class HomeToolbarManagerImpl(
        val identityManager: IdentityManager
) : HomeToolbarManager {

    override fun getPresentation(rankingCriteria: RankingCriteria?): Presentation {
        return Presentation(rankingCriteria?.rankingActivityDayLimit != null &&
                rankingCriteria.rankingNumTourneysAttended != null &&
                rankingCriteria.tournamentQualifiedDayLimit != null,
                identityManager.hasIdentity)
    }

}
