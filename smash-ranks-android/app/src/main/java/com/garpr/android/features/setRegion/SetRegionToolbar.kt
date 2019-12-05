package com.garpr.android.features.setRegion

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import com.garpr.android.R
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.features.common.views.GarToolbar
import kotlinx.android.synthetic.main.gar_toolbar.view.*
import kotlinx.android.synthetic.main.set_region_toolbar_items.view.*

class SetRegionToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : GarToolbar(context, attrs) {

    var enableSaveIcon: Boolean
        get() = saveButton.isEnabled
        set(value) {
            saveButton.isEnabled = value
        }

    var showSaveIcon: Boolean
        get() = saveButton.visibility == View.VISIBLE
        set(value) {
            saveButton.visibility = if (value) View.VISIBLE else View.GONE
        }

    var listener: Listener? = null

    private val saveClickListener = OnClickListener {
        listener?.onSaveClick(this)
    }

    interface Listener {
        fun onSaveClick(v: SetRegionToolbar)
    }

    init {
        layoutInflater.inflate(R.layout.set_region_toolbar_items, menuExpansionContainer)
        saveButton.setOnClickListener(saveClickListener)
    }

}
