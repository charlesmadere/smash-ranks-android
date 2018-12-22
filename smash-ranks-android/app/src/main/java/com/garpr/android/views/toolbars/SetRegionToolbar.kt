package com.garpr.android.views.toolbars

import android.content.Context
import android.util.AttributeSet

class SetRegionToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : GarToolbar(context, attrs) {

    interface Listeners {
        val enableSaveIcon: Boolean
        val showSaveIcon: Boolean
        fun onSaveClick(v: SetRegionToolbar)
    }

    override fun refresh() {
        super.refresh()


    }

}
