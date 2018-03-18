package com.garpr.android.views.toolbars

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.util.SparseBooleanArray
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.Refreshable

abstract class MenuToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : Toolbar(context, attrs), Heartbeat, Refreshable {

    private val animationDuration: Long by lazy {
        resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
    }

    private val sparseMenuItemsArray = SparseBooleanArray()
    private val toolbarUtils: ToolbarUtils by lazy { ToolbarUtils(this) }


    private fun createSparseMenuItemsArray() {
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            sparseMenuItemsArray.put(menuItem.itemId, menuItem.isVisible)
        }
    }

    fun fadeInTitleAndSubtitle(title: CharSequence, subtitle: CharSequence) {
        val titleTextView = toolbarUtils.titleTextView
        val subtitleTextView = toolbarUtils.subtitleTextView

        if (titleTextView == null || subtitleTextView == null) {
            this.title = title
            this.subtitle = subtitle
            return
        }


        // TODO
    }

    fun fadeOutTitleAndSubtitle() {
        val titleTextView = toolbarUtils.titleTextView
        val subtitleTextView = toolbarUtils.subtitleTextView

        if (titleTextView == null || subtitleTextView == null) {
            title = ""
            subtitle = ""
            return
        }

        // TODO
    }

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

    protected var isMenuCreated: Boolean = false
        private set

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        refresh()
    }

    open fun onCreateOptionsMenu(inflater: MenuInflater, menu: Menu) {
        createSparseMenuItemsArray()
        isMenuCreated = true
    }

    open fun onOptionsItemSelected(item: MenuItem): Boolean {
        // intentionally empty, children can override
        return false
    }

    protected open fun onRefreshMenu() {
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            menuItem.isVisible = sparseMenuItemsArray.get(menuItem.itemId)
        }
    }

    protected fun postRefresh() {
        if (isAlive) {
            post(refreshRunnable)
        }
    }

    final override fun refresh() {
        if (isMenuCreated) {
            onRefreshMenu()
        }
    }

    private val refreshRunnable = Runnable {
        if (isAlive) {
            refresh()
        }
    }

}
