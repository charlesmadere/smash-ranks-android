package com.garpr.android.misc

import android.content.Context
import com.garpr.android.misc.HomeToolbarManager.Presentation

class HomeToolbarManagerImpl(
        val identityManager: IdentityManager,
        val regionManager: RegionManager
) : HomeToolbarManager {

    override fun getPresentation(context: Context): Presentation {
        val presentation = Presentation()

        val region = regionManager.getRegion(context)
        presentation.mIsActivityRequirementsVisible = region.hasActivityRequirements
        presentation.mIsViewYourselfVisible = identityManager.hasIdentity

        return presentation
    }

}
