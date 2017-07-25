package com.garpr.android.adapters

import android.content.Context
import android.support.annotation.LayoutRes

import com.garpr.android.R
import com.garpr.android.extensions.orFalse
import com.garpr.android.models.Region
import com.garpr.android.models.RegionsBundle

class RegionsSelectionAdapter(context: Context) : BaseAdapter<Region>(context) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    @LayoutRes
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_region_selection
    }

    fun set(bundle: RegionsBundle?) {
        val regions = bundle?.regions

        if (regions?.isNotEmpty().orFalse()) {
            set(regions)
        } else {
            clear()
        }
    }

}
