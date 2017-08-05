package com.garpr.android.adapters

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class BaseAdapter<T>(
        private val mLayoutInflater: LayoutInflater
) : RecyclerView.Adapter<BaseAdapterViewHolder>() {

    private val mItems: MutableList<T> = mutableListOf()


    constructor(context: Context) : this(LayoutInflater.from(context))

    fun add(item: T?) {
        if (item != null) {
            mItems.add(item)
            notifyDataSetChanged()
        }
    }

    fun add(items: List<T>?) {
        if (items != null && !items.isEmpty()) {
            for (item in items) {
                if (item != null) {
                    mItems.add(item)
                }
            }

            notifyDataSetChanged()
        }
    }

    fun clear() {
        if (!mItems.isEmpty()) {
            mItems.clear()
            notifyDataSetChanged()
        }
    }

    fun getItem(position: Int): T {
        return mItems[position]
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    @LayoutRes
    abstract override fun getItemViewType(position: Int): Int

    val isEmpty: Boolean
        get() = mItems.isEmpty()

    override fun onBindViewHolder(holder: BaseAdapterViewHolder, position: Int) {
        val item = mItems[position]
        (holder.itemView as BaseAdapterView<T>).setContent(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapterViewHolder {
        val view = mLayoutInflater.inflate(viewType, parent, false)
        return BaseAdapterViewHolder(view)
    }

    fun set(items: List<T>?) {
        mItems.clear()

        if (items != null && items.isNotEmpty()) {
            mItems.addAll(items)
        }

        notifyDataSetChanged()
    }

}
