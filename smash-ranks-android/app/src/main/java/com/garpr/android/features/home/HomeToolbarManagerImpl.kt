package com.garpr.android.features.home

import com.garpr.android.data.models.RankingCriteria
import com.garpr.android.features.home.HomeToolbarManager.Presentation
import com.garpr.android.repositories.IdentityManager

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
