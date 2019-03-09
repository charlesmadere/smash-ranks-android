package com.garpr.android.adapters

import androidx.annotation.LayoutRes
import com.garpr.android.R
import com.garpr.android.misc.Timber

class TimberEntriesAdapter : BaseAdapter<Timber.Entry>() {

    @LayoutRes
    override fun getItemViewType(position: Int) = R.layout.item_timber_entry

}
