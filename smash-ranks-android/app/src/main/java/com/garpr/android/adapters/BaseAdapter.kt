package com.garpr.android.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(
        private val layoutInflater: LayoutInflater
) : RecyclerView.Adapter<BaseAdapterViewHolder>() {

    private val items = mutableListOf<T>()


    constructor(context: Context) : this(LayoutInflater.from(context))

    open fun clear() {
        if (!isEmpty) {
            items.clear()
            notifyDataSetChanged()
        }
    }

    val context: Context
        get() = layoutInflater.context

    fun getItem(position: Int) = items[position]

    override fun getItemCount() = items.size

    @LayoutRes
    abstract override fun getItemViewType(position: Int): Int

    val isEmpty: Boolean
        get() = items.isEmpty()

    override fun onBindViewHolder(holder: BaseAdapterViewHolder, position: Int) {
        val item = items[position]

        @Suppress("UNCHECKED_CAST")
        val view = holder.itemView as? BaseAdapterView<T> ?: throw ClassCastException(
                "itemView is an unknown type: ${holder.itemView}")

        view.setContent(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapterViewHolder {
        val view = layoutInflater.inflate(viewType, parent, false)
        return BaseAdapterViewHolder(view)
    }

    open fun set(items: List<T>?) {
        this.items.clear()

        if (items?.isNotEmpty() == true) {
            this.items.addAll(items)
        }

        notifyDataSetChanged()
    }

}
