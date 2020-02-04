package com.garpr.android.features.setIdentity

import android.content.Context
import android.util.AttributeSet
import android.view.View.OnClickListener
import com.garpr.android.R
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.features.common.views.SearchToolbar
import kotlinx.android.synthetic.main.gar_toolbar.view.*
import kotlinx.android.synthetic.main.set_identity_toolbar_items.view.*

class SetIdentityToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SearchToolbar(context, attrs) {

    var enableSaveIcon: Boolean
        get() = saveButton.isEnabled
        set(value) {
            saveButton.isEnabled = value
        }

    var showSaveIcon: Boolean
        get() = saveButton.visibility == VISIBLE
        set(value) {
            saveButton.visibility = if (value) VISIBLE else GONE
        }

    var listener: Listener? = null

    private val saveClickListener = OnClickListener {
        listener?.onSaveClick(this)
    }

    init {
        layoutInflater.inflate(R.layout.set_identity_toolbar_items, menuExpansionContainer)
        saveButton.setOnClickListener(saveClickListener)
    }

    interface Listener {
        fun onSaveClick(v: SetIdentityToolbar)
    }

}
