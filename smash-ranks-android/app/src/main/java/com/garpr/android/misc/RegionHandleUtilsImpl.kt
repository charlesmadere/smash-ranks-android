package com.garpr.android.misc

import android.content.Context
import android.content.ContextWrapper
import com.garpr.android.data.models.Region
import com.garpr.android.repositories.RegionRepository

class RegionHandleUtilsImpl(
        private val regionRepository: RegionRepository
) : RegionHandleUtils {

    @Suppress("IfThenToElvis")
    override fun getRegion(context: Context?): Region {
        val region = (context as? RegionHandle?)?.regionOverride

        return if (region != null) {
            region
        } else if (context is ContextWrapper) {
            getRegion(context.baseContext)
        } else {
            regionRepository.region
        }
    }

}
