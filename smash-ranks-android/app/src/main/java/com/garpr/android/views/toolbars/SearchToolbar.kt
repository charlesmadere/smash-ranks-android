package com.garpr.android.views.toolbars

import android.content.Context
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.garpr.android.R
import com.garpr.android.extensions.optActivity
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable

abstract class SearchToolbar : MenuToolbar, MenuItemCompat.OnActionExpandListener,
        SearchQueryHandle, SearchView.OnQueryTextListener {

    private var mSearchMenuItem: MenuItem? = null
    private var mSearchView: SearchView? = null


    interface Listener {
        val showSearchMenuItem: Boolean
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
            attrs, defStyleAttr)

    fun closeSearchLayout() {
        if (isSearchLayoutExpanded) {
            mSearchMenuItem?.let {
                MenuItemCompat.collapseActionView(it)
            }
        }
    }

    override val searchQuery: CharSequence?
        get() {
            mSearchView?.let {
                return it.query
            } ?: return null
        }

    val isSearchLayoutExpanded: Boolean
        get() {
            mSearchMenuItem?.let {
                return MenuItemCompat.isActionViewExpanded(it)
            } ?: return false
        }

    override fun onCreateOptionsMenu(inflater: MenuInflater, menu: Menu) {
        mSearchMenuItem = menu.findItem(R.id.miSearch)

        mSearchMenuItem?.let {
            MenuItemCompat.setOnActionExpandListener(it, this)
        } ?: throw RuntimeException("mSearchMenuItem is null")

        mSearchView = MenuItemCompat.getActionView(mSearchMenuItem) as SearchView

        mSearchView?.let {
            it.queryHint = resources.getText(R.string.search_)
            it.setOnQueryTextListener(this)
        }

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

        mSearchMenuItem?.let {
            if (isSearchLayoutExpanded) {
                for (i in 0..menu.size() - 1) {
                    val menuItem = menu.getItem(i)

                    if (menuItem.itemId != it.itemId) {
                        menuItem.isVisible = false
                    }
                }
            } else {
                val activity = context.optActivity()
                it.isVisible = activity is Listener && (activity as Listener).showSearchMenuItem
            }
        } ?: throw RuntimeException("mSearchMenuItem is null")
    }

}
