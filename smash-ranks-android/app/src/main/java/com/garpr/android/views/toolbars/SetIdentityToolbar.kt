package com.garpr.android.views.toolbars

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.garpr.android.R
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.layoutInflater
import kotlinx.android.synthetic.main.gar_toolbar.view.*
import kotlinx.android.synthetic.main.set_identity_toolbar_items.view.*

class SetIdentityToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SearchToolbar(context, attrs) {

    interface Listeners {
        val enableSaveIcon: Boolean
        val showSaveIcon: Boolean
        fun onSaveClick(v: SetIdentityToolbar)
    }

    init {
        layoutInflater.inflate(R.layout.set_identity_toolbar_items, menuExpansionContainer)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        saveButton.setOnClickListener {
            (activity as? Listeners)?.onSaveClick(this)
        }
    }

    override fun refresh() {
        super.refresh()

        val listeners = activity as? Listeners
        saveButton.visibility = if (listeners?.showSaveIcon == true) View.VISIBLE else View.GONE
        saveButton.isEnabled = listeners?.enableSaveIcon == true
    }

}
