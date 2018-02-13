package com.garpr.android.views.toolbars

import android.content.Context
import android.support.annotation.AttrRes
import android.support.v4.view.ViewCompat
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.util.SparseBooleanArray
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

import com.garpr.android.misc.Heartbeat

abstract class MenuToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr), Heartbeat {

    private val sparseMenuItemsArray = SparseBooleanArray()


    private fun createSparseMenuItemsArray() {
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            sparseMenuItemsArray.put(menuItem.itemId, menuItem.isVisible)
        }
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

        refreshMenu()
    }

    open fun onCreateOptionsMenu(inflater: MenuInflater, menu: Menu) {
        createSparseMenuItemsArray()
        isMenuCreated = true
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean {
        // intentionally empty, children can override
        return false
    }

    open fun onRefreshMenu() {
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            menuItem.isVisible = sparseMenuItemsArray.get(menuItem.itemId)
        }
    }

    protected fun postRefreshMenu() {
        if (isAlive) {
            post {
                if (isAlive) {
                    refreshMenu()
                }
            }
        }
    }

    fun refreshMenu() {
        if (isMenuCreated) {
            onRefreshMenu()
        }
    }

}
