package com.garpr.android.adapters

import android.content.Context
import androidx.annotation.LayoutRes

abstract class BaseMultiAdapter(
        context: Context,
        private val layoutKeyMap: Map<Class<*>, Int>
) : BaseAdapter<Any>(context) {

    @LayoutRes
    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return layoutKeyMap[item.javaClass] ?: throw RuntimeException(
                "itemViewType is null (item $item) (position $position)")
    }

}
