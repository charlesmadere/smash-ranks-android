package com.garpr.android.features.home

import com.garpr.android.data.models.RankingCriteria
import com.garpr.android.features.home.HomeToolbarManager.Presentation
import com.garpr.android.repositories.IdentityRepository

class HomeToolbarManagerImpl(
        private val identityRepository: IdentityRepository
) : HomeToolbarManager {

    override fun getPresentation(rankingCriteria: RankingCriteria?): Presentation {
        return Presentation(
                isActivityRequirementsVisible = rankingCriteria?.rankingActivityDayLimit != null &&
                        rankingCriteria.rankingNumTourneysAttended != null &&
                        rankingCriteria.tournamentQualifiedDayLimit != null,
                isViewYourselfVisible = identityRepository.hasIdentity
        )
    }

}
