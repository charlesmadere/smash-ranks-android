package com.garpr.android.views.toolbars

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.extensions.activity
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import kotlinx.android.synthetic.main.gar_toolbar.view.*
import kotlinx.android.synthetic.main.search_toolbar_items.view.*

open class SearchToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : GarToolbar(context, attrs), SearchQueryHandle, TextView.OnEditorActionListener, TextWatcher {

    interface Listener {
        val showSearchIcon: Boolean
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.search_toolbar_items, menuExpansionContainer)
    }

    override fun afterTextChanged(s: Editable?) {
        (activity as? Searchable)?.search(searchQuery?.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // intentionally empty
    }

    fun closeSearchField() {
        if (isSearchFieldExpanded) {
            searchField.clearFocus()
            searchField.visibility = View.INVISIBLE
            showTitleContainer = true
            refreshSearchIcon()
        }
    }

    val isSearchFieldExpanded: Boolean
        get() = searchField.visibility == View.VISIBLE

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            (activity as? Searchable)?.search(searchQuery?.toString())
        }

        return false
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        searchIcon.setOnClickListener {
            openSearchField()
        }

        searchField.addTextChangedListener(this)
        searchField.setOnEditorActionListener(this)
    }

    private fun openSearchField() {
        if (isSearchFieldExpanded) {
            return
        }

        showTitleContainer = false
        searchIcon.visibility = View.GONE
        searchField.visibility = View.VISIBLE
        searchField.requestFocus()
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // intentionally empty
    }

    override fun refresh() {
        super.refresh()
        closeSearchField()
    }

    private fun refreshSearchIcon() {
        if ((activity as? Listener)?.showSearchIcon == true) {
            searchIcon.visibility = View.VISIBLE
        } else {
            searchIcon.visibility = View.GONE
        }
    }

    override val searchQuery: CharSequence?
        get() = searchField.text

    override fun upNavigate() {
        if (isSearchFieldExpanded) {
            closeSearchField()
        } else {
            super.upNavigate()
        }
    }

}
