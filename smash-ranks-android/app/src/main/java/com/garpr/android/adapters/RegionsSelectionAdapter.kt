package com.garpr.android.adapters

import android.content.Context
import android.support.annotation.LayoutRes
import com.garpr.android.R
import com.garpr.android.models.LiteRegion
import com.garpr.android.models.RegionsBundle

class RegionsSelectionAdapter(context: Context) : BaseAdapter<LiteRegion>(context) {

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

    fun set(content: RegionsBundle?) {
        set(content?.regions)
    }

}
