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

    private var mSearchMenuItem: MenuItem? = null
    private var mSearchView: SearchView? = null


    interface Listener {
        val showSearchMenuItem: Boolean
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    fun closeSearchLayout() {
        if (isSearchLayoutExpanded) {
            mSearchMenuItem?.collapseActionView()
        }
    }

    override val searchQuery: CharSequence?
        get() = mSearchView?.query

    val isSearchLayoutExpanded: Boolean
        get() = mSearchMenuItem?.isActionViewExpanded == true

    override fun onCreateOptionsMenu(inflater: MenuInflater, menu: Menu) {
        val searchMenuItem = menu.findItem(R.id.miSearch) ?: throw RuntimeException(
                "searchMenuItem is null")
        searchMenuItem.setOnActionExpandListener(this)
        mSearchMenuItem = searchMenuItem

        val searchView = searchMenuItem.actionView as SearchView
        searchView.queryHint = resources.getText(R.string.search_)
        searchView.setOnQueryTextListener(this)
        mSearchView = searchView

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
        val activity = context.optActivity()

        if (activity is Searchable) {
            (activity as Searchable).search(newText)
        }

        return false
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        onQueryTextChange(query)
        return false
    }

    override fun onRefreshMenu() {
        super.onRefreshMenu()

        val searchMenuItem = mSearchMenuItem ?: throw RuntimeException("mSearchMenuItem is null")

        if (isSearchLayoutExpanded) {
            for (i in 0..menu.size() - 1) {
                val menuItem = menu.getItem(i)

                if (menuItem.itemId != searchMenuItem.itemId) {
                    menuItem.isVisible = false
                }
            }
        } else {
            val activity = context.optActivity()
            searchMenuItem.isVisible = activity is Listener && (activity as Listener).showSearchMenuItem
        }
    }

}
