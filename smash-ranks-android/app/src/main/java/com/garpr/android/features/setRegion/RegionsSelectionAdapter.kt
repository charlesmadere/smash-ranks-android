package com.garpr.android.features.setRegion

import com.garpr.android.R
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.features.common.adapters.BaseMultiAdapter

class RegionsSelectionAdapter : BaseMultiAdapter(LAYOUT_KEY_MAP) {

    companion object {
        private val LAYOUT_KEY_MAP = mapOf<Class<*>, Int>(
                Endpoint::class.java to R.layout.divider_endpoint,
                Region::class.java to R.layout.item_region_selection
        )
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}
