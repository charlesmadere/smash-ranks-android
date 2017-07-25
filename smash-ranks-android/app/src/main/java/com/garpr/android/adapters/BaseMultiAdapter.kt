package com.garpr.android.adapters

import android.content.Context
import android.support.annotation.LayoutRes

abstract class BaseMultiAdapter(
        context: Context,
        layoutKeyMap: Map<Class<*>, Int>
) : BaseAdapter<Any>(
        context
) {

    private val mLayoutKeyMap: Map<Class<*>, Int> = layoutKeyMap


    @LayoutRes
    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        val itemViewType = mLayoutKeyMap[item.javaClass] ?: throw RuntimeException(
                "itemViewType is null (item $item) (position $position)")

        return itemViewType
    }

}
