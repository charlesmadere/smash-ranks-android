package com.garpr.android.views.toolbars

import android.content.Context
import android.util.AttributeSet
import com.garpr.android.extensions.activity

class SetIdentityToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SearchToolbar(context, attrs) {

    interface Listeners {
        val enableSaveIcon: Boolean
        val showSaveIcon: Boolean
        fun onSaveClick(v: SetIdentityToolbar)
    }

    override fun refresh() {
        super.refresh()

        if ((activity as? Listeners)?.enableSaveIcon == true) {

        } else {

        }
    }

}
