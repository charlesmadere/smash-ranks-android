package com.garpr.android.features.setIdentity

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import com.garpr.android.R
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.views.toolbars.SearchToolbar
import kotlinx.android.synthetic.main.gar_toolbar.view.*
import kotlinx.android.synthetic.main.set_identity_toolbar_items.view.*

class SetIdentityToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SearchToolbar(context, attrs) {

    private val onSaveClickListener = OnClickListener {
        (activity as? Listeners?)?.onSaveClick(this)
    }

    interface Listeners {
        val enableSaveIcon: Boolean
        val showSaveIcon: Boolean
        fun onSaveClick(v: SetIdentityToolbar)
    }

    init {
        layoutInflater.inflate(R.layout.set_identity_toolbar_items, menuExpansionContainer)
        saveButton.setOnClickListener(onSaveClickListener)
    }

    override fun refresh() {
        super.refresh()

        val listeners = activity as? Listeners?
        saveButton.visibility = if (listeners?.showSaveIcon == true) View.VISIBLE else View.GONE
        saveButton.isEnabled = listeners?.enableSaveIcon == true
    }

}
