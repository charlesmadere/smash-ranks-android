package com.garpr.android.misc

import android.content.Context
import com.garpr.android.misc.HomeToolbarManager.Presentation

class HomeToolbarManagerImpl(
        val identityManager: IdentityManager,
        val regionManager: RegionManager
) : HomeToolbarManager {

    override fun getPresentation(context: Context): Presentation {
        val region = regionManager.getRegion(context)
        return Presentation(region.hasActivityRequirements, identityManager.hasIdentity)
    }

}
