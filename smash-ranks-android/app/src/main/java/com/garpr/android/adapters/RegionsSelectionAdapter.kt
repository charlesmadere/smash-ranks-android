package com.garpr.android.adapters

import android.content.Context
import android.support.annotation.LayoutRes
import com.garpr.android.R
import com.garpr.android.models.AbsRegion
import com.garpr.android.models.RegionsBundle

class RegionsSelectionAdapter(context: Context) : BaseAdapter<AbsRegion>(context) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    @LayoutRes
    override fun getItemViewType(position: Int) = R.layout.item_region_selection

    fun set(content: RegionsBundle?) {
        set(content?.regions)
    }

}
