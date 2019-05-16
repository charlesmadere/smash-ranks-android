package com.garpr.android.adapters

import com.garpr.android.R
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region

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
