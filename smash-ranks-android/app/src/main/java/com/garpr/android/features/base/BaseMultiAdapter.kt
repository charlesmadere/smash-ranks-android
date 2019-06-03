package com.garpr.android.features.base

import androidx.annotation.LayoutRes

abstract class BaseMultiAdapter(
        private val layoutKeyMap: Map<Class<*>, Int>
) : BaseAdapter<Any>() {

    @LayoutRes
    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return layoutKeyMap[item.javaClass] ?: throw RuntimeException(
                "itemViewType is null (item $item) (position $position)")
    }

}
