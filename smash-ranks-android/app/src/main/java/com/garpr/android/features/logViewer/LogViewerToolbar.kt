package com.garpr.android.features.logViewer

import android.content.Context
import android.util.AttributeSet
import com.garpr.android.R
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.features.common.GarToolbar
import kotlinx.android.synthetic.main.gar_toolbar.view.*
import kotlinx.android.synthetic.main.log_viewer_toolbar_items.view.*

class LogViewerToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : GarToolbar(context, attrs) {

    interface Listeners {
        val enableClearButton: Boolean
        fun onClearClick(v: LogViewerToolbar)
    }

    init {
        layoutInflater.inflate(R.layout.log_viewer_toolbar_items, menuExpansionContainer)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        clearButton.setOnClickListener {
            (activity as? Listeners?)?.onClearClick(this)
        }
    }

    override fun refresh() {
        super.refresh()

        clearButton.isEnabled = (activity as? Listeners?)?.enableClearButton == true
    }

}
