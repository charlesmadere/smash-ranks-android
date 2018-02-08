package com.garpr.android.views.toolbars

import android.content.Context
import android.support.annotation.AttrRes
import android.support.v7.widget.SearchView
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.garpr.android.R
import com.garpr.android.extensions.optActivity
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable

abstract class SearchToolbar : MenuToolbar, MenuItem.OnActionExpandListener, SearchQueryHandle,
        SearchView.OnQueryTextListener {

    private var searchMenuItem: MenuItem? = null
    private var searchView: SearchView? = null


    interface Listener {
        val showSearchMenuItem: Boolean
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    fun closeSearchLayout() {
        if (isSearchLayoutExpanded) {
            searchMenuItem?.collapseActionView()
        }
    }

    val isSearchLayoutExpanded: Boolean
        get() = searchMenuItem?.isActionViewExpanded == true

    override fun onCreateOptionsMenu(inflater: MenuInflater, menu: Menu) {
        val searchMenuItem = menu.findItem(R.id.miSearch) ?: throw NullPointerException(
                "searchMenuItem is null")
        searchMenuItem.setOnActionExpandListener(this)
        this.searchMenuItem = searchMenuItem

        val searchView = searchMenuItem.actionView as SearchView
        searchView.queryHint = resources.getText(R.string.search_)
        searchView.setOnQueryTextListener(this)
        this.searchView = searchView

        super.onCreateOptionsMenu(inflater, menu)
    }

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        postRefreshMenu()
        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
        postRefreshMenu()
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        (context.optActivity() as? Searchable)?.search(newText)
        return false
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        onQueryTextChange(query)
        return false
    }

    override fun onRefreshMenu() {
        super.onRefreshMenu()

        val searchMenuItem = this.searchMenuItem ?: throw IllegalStateException(
                "searchMenuItem is null")

        if (isSearchLayoutExpanded) {
            for (i in 0 until menu.size()) {
                val menuItem = menu.getItem(i)

                if (menuItem.itemId != searchMenuItem.itemId) {
                    menuItem.isVisible = false
                }
            }
        } else {
            val activity = context.optActivity()
            searchMenuItem.isVisible = (activity as? Listener)?.showSearchMenuItem == true
        }
    }

    override val searchQuery: CharSequence?
        get() = searchView?.query

}
