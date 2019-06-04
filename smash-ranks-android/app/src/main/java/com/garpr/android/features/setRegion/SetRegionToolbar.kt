package com.garpr.android.features.setRegion

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.garpr.android.R
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.features.common.views.GarToolbar
import kotlinx.android.synthetic.main.gar_toolbar.view.*
import kotlinx.android.synthetic.main.set_region_toolbar_items.view.*

class SetRegionToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : GarToolbar(context, attrs) {

    interface Listeners {
        val enableSaveIcon: Boolean
        val showSaveIcon: Boolean
        fun onSaveClick(v: SetRegionToolbar)
    }

    init {
        layoutInflater.inflate(R.layout.set_region_toolbar_items, menuExpansionContainer)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        saveButton.setOnClickListener {
            (activity as? Listeners?)?.onSaveClick(this)
        }
    }

    override fun refresh() {
        super.refresh()

        val listeners = activity as? Listeners?
        saveButton.visibility = if (listeners?.showSaveIcon == true) View.VISIBLE else View.GONE
        saveButton.isEnabled = listeners?.enableSaveIcon == true
    }

}
