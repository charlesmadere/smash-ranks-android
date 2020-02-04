package com.garpr.android.features.home

import android.content.Context
import android.util.AttributeSet
import android.view.MenuItem
import androidx.appcompat.widget.PopupMenu
import com.garpr.android.R
import com.garpr.android.extensions.addMenuItem
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.features.common.views.SearchToolbar
import kotlinx.android.synthetic.main.gar_toolbar.view.*
import kotlinx.android.synthetic.main.home_toolbar_items.view.*

class HomeToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SearchToolbar(context, attrs) {

    var isActivityRequirementsVisible: Boolean
        get() = showActivityRequirements.isVisible
        set(value) {
            showActivityRequirements.isVisible = value
        }

    var listeners: Listeners? = null

    private val showActivityRequirements: MenuItem

    private val activityRequirementsClickListener = MenuItem.OnMenuItemClickListener {
        listeners?.onActivityRequirementsClick(this)
        true
    }

    private val shareClickListener = MenuItem.OnMenuItemClickListener {
        listeners?.onShareClick(this)
        true
    }

    private val viewAllPlayersClickListener = MenuItem.OnMenuItemClickListener {
        listeners?.onViewAllPlayersClick(this)
        true
    }

    private val settingsClickListener = MenuItem.OnMenuItemClickListener {
        listeners?.onSettingsClick(this)
        true
    }

    private val overflowPopupMenu: PopupMenu

    interface Listeners {
        fun onActivityRequirementsClick(v: HomeToolbar)
        fun onSettingsClick(v: HomeToolbar)
        fun onShareClick(v: HomeToolbar)
        fun onViewAllPlayersClick(v: HomeToolbar)
    }

    init {
        layoutInflater.inflate(R.layout.home_toolbar_items, menuExpansionContainer)

        overflowPopupMenu = PopupMenu(context, overflowButton)
        overflowButton.setOnClickListener { overflowPopupMenu.show() }
        overflowButton.setOnTouchListener(overflowPopupMenu.dragToOpenListener)

        overflowPopupMenu.addMenuItem(R.string.share, shareClickListener)
        showActivityRequirements = overflowPopupMenu.addMenuItem(R.string.activity_requirements,
                    activityRequirementsClickListener)
        overflowPopupMenu.addMenuItem(R.string.view_all_players, viewAllPlayersClickListener)
        overflowPopupMenu.addMenuItem(R.string.settings, settingsClickListener)
    }

    override fun onCloseSearchField() {
        super.onCloseSearchField()
        overflowButton.visibility = VISIBLE
    }

    override fun onOpenSearchField() {
        super.onOpenSearchField()
        overflowButton.visibility = GONE
    }

}
