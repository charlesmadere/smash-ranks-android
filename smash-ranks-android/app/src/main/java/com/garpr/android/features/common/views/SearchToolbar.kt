package com.garpr.android.features.common.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.hideKeyboard
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.requestFocusAndOpenKeyboard
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import kotlinx.android.synthetic.main.gar_toolbar.view.*
import kotlinx.android.synthetic.main.search_toolbar_items.view.*

open class SearchToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : GarToolbar(context, attrs), SearchQueryHandle, TextView.OnEditorActionListener, TextWatcher {

    private val wasShowingUpNavigation = showUpNavigation


    interface Listener {
        val showSearchIcon: Boolean
    }

    init {
        layoutInflater.inflate(R.layout.search_toolbar_items, menuExpansionContainer)
    }

    override fun afterTextChanged(s: Editable?) {
        (activity as? Searchable?)?.search(searchQuery?.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // intentionally empty
    }

    fun closeSearchField() {
        if (isSearchFieldExpanded) {
            onCloseSearchField()
        }
    }

    val isSearchFieldExpanded: Boolean
        get() = searchField.visibility == View.VISIBLE

    protected open fun onCloseSearchField() {
        activity?.hideKeyboard()
        searchField.clear()
        searchField.visibility = View.GONE
        showUpNavigation = wasShowingUpNavigation

        menuExpansionContainer.layoutParams = (menuExpansionContainer.layoutParams as LayoutParams).apply {
            startToEnd = View.NO_ID
            width = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        showTitleContainer = true
        refreshSearchIcon()
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            (activity as? Searchable?)?.search(searchQuery?.toString())
            activity?.hideKeyboard()
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

    protected open fun onOpenSearchField() {
        showTitleContainer = false
        searchIcon.visibility = View.GONE
        showUpNavigation = true

        menuExpansionContainer.layoutParams = (menuExpansionContainer.layoutParams as LayoutParams).apply {
            startToEnd = toolbarSpace.id
            width = LayoutParams.MATCH_CONSTRAINT
        }

        searchField.visibility = View.VISIBLE
        searchField.requestFocusAndOpenKeyboard()
    }

    private fun openSearchField() {
        if (!isSearchFieldExpanded) {
            onOpenSearchField()
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // intentionally empty
    }

    override fun refresh() {
        super.refresh()
        refreshSearchIcon()
    }

    private fun refreshSearchIcon() {
        if ((activity as? Listener?)?.showSearchIcon == true) {
            if (!isSearchFieldExpanded) {
                searchIcon.visibility = View.VISIBLE
            }
        } else {
            closeSearchField()
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