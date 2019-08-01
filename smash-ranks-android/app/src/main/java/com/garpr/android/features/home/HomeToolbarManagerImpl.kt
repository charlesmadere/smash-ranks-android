package com.garpr.android.features.home

import com.garpr.android.data.models.AbsRegion
import com.garpr.android.features.home.HomeToolbarManager.Presentation
import com.garpr.android.repositories.IdentityRepository

class HomeToolbarManagerImpl(
        private val identityRepository: IdentityRepository
) : HomeToolbarManager {

    override fun getPresentation(region: AbsRegion?): Presentation {
        return Presentation(
                isActivityRequirementsVisible = region?.rankingActivityDayLimit != null &&
                        region.rankingNumTourneysAttended != null &&
                        region.tournamentQualifiedDayLimit != null,
                isViewYourselfVisible = identityRepository.hasIdentity
        )
    }

}
