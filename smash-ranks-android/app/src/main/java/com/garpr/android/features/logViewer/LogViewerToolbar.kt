package com.garpr.android.features.logViewer

import android.content.Context
import android.util.AttributeSet
import android.view.View.OnClickListener
import com.garpr.android.R
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.features.common.views.GarToolbar
import kotlinx.android.synthetic.main.gar_toolbar.view.*
import kotlinx.android.synthetic.main.log_viewer_toolbar_items.view.*

class LogViewerToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : GarToolbar(context, attrs) {

    var isClearEnabled: Boolean
        get() = clearButton.isEnabled
        set(value) {
            clearButton.isEnabled = value
        }

    var listener: Listener? = null

    private val clearClickListener = OnClickListener {
        listener?.onClearClick(this)
    }

    interface Listener {
        fun onClearClick(v: LogViewerToolbar)
    }

    init {
        layoutInflater.inflate(R.layout.log_viewer_toolbar_items, menuExpansionContainer)
        clearButton.setOnClickListener(clearClickListener)
    }

}
