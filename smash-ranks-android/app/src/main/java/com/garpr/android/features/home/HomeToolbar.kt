package com.garpr.android.features.home

import android.content.Context
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View.OnClickListener
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

    var listeners: Listeners? = null

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

    private val overflowButtonOnClickListener = OnClickListener {
        overflowPopupMenu.show()
    }

    private val overflowPopupMenu: PopupMenu

    init {
        layoutInflater.inflate(R.layout.home_toolbar_items, menuExpansionContainer)

        overflowPopupMenu = PopupMenu(context, overflowButton)
        overflowButton.setOnClickListener(overflowButtonOnClickListener)
        overflowButton.setOnTouchListener(overflowPopupMenu.dragToOpenListener)

        overflowPopupMenu.addMenuItem(R.string.share, shareClickListener)
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

    interface Listeners {
        fun onSettingsClick(v: HomeToolbar)
        fun onShareClick(v: HomeToolbar)
        fun onViewAllPlayersClick(v: HomeToolbar)
    }

}
