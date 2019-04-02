package com.garpr.android.managers

import com.garpr.android.data.models.RankingCriteria
import com.garpr.android.managers.HomeToolbarManager.Presentation

class HomeToolbarManagerImpl(
        private val identityManager: IdentityManager
) : HomeToolbarManager {

    override fun getPresentation(rankingCriteria: RankingCriteria?): Presentation {
        return Presentation(
                isActivityRequirementsVisible = rankingCriteria?.rankingActivityDayLimit != null &&
                        rankingCriteria.rankingNumTourneysAttended != null &&
                        rankingCriteria.tournamentQualifiedDayLimit != null,
                isViewYourselfVisible = identityManager.hasIdentity
        )
    }

}
