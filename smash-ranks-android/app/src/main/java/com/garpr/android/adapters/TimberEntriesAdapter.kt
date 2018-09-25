package com.garpr.android.adapters

import android.content.Context
import androidx.annotation.LayoutRes

import com.garpr.android.R
import com.garpr.android.misc.Timber

class TimberEntriesAdapter(context: Context) : BaseAdapter<Timber.Entry>(context) {

    @LayoutRes
    override fun getItemViewType(position: Int) = R.layout.item_timber_entry

}
