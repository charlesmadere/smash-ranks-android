package com.garpr.android.views.toolbars

import android.content.Context
import android.util.AttributeSet

class LogViewerToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : GarToolbar(context, attrs) {

    interface Listeners {
        val enableClearButton: Boolean
        fun onClearClick(v: LogViewerToolbar)
    }



}
