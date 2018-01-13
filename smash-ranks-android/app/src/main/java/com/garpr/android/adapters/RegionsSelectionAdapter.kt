package com.garpr.android.adapters

import android.content.Context
import com.garpr.android.R
import com.garpr.android.models.Endpoint
import com.garpr.android.models.Region

class RegionsSelectionAdapter(context: Context) : BaseMultiAdapter(context, LAYOUT_KEY_MAP) {

    companion object {
        private val LAYOUT_KEY_MAP = mapOf<Class<*>, Int>(
                Endpoint::class.java to R.layout.divider_endpoint,
                Region::class.java to R.layout.item_region_selection)
    }

}
