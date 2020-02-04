package com.garpr.android.features.common.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.hideKeyboard
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.requestFocusAndOpenKeyboard
import com.garpr.android.misc.AbstractTextWatcher
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import kotlinx.android.synthetic.main.gar_toolbar.view.*
import kotlinx.android.synthetic.main.search_toolbar_items.view.*

open class SearchToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : GarToolbar(context, attrs), SearchQueryHandle {

    val isSearchFieldExpanded: Boolean
        get() = searchField.visibility == VISIBLE

    var showSearchIcon: Boolean
        get() = searchIcon.visibility == VISIBLE
        set(value) {
            searchIcon.visibility = if (!searchField.isFocused && value) VISIBLE else GONE
        }

    private val wasShowingUpNavigation = showUpNavigation

    override val searchQuery: CharSequence?
        get() = searchField.text

    private val searchIconClickListener = OnClickListener {
        openSearchField()
    }

    var searchableListener: Searchable? = null

    private val searchFieldActionListener = TextView.OnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchableListener?.search(searchQuery?.toString())
            activity?.hideKeyboard()
        }

        false
    }

    private val searchFieldTextWatcher: TextWatcher = object : AbstractTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            searchableListener?.search(searchQuery?.toString())
        }
    }

    init {
        layoutInflater.inflate(R.layout.search_toolbar_items, menuExpansionContainer)
        searchIcon.setOnClickListener(searchIconClickListener)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.SearchToolbar)

        if (ta.hasValue(R.styleable.SearchToolbar_android_hint)) {
            searchField.hint = ta.getText(R.styleable.SearchToolbar_android_hint)
        }

        ta.recycle()

        searchField.addTextChangedListener(searchFieldTextWatcher)
        searchField.setOnEditorActionListener(searchFieldActionListener)
    }

    fun closeSearchField() {
        if (isSearchFieldExpanded) {
            onCloseSearchField()
        }
    }

    protected open fun onCloseSearchField() {
        activity?.hideKeyboard()
        searchField.clear()
        searchField.visibility = GONE
        showUpNavigation = wasShowingUpNavigation

        menuExpansionContainer.layoutParams = (menuExpansionContainer.layoutParams as LayoutParams).apply {
            startToEnd = NO_ID
            width = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        showTitleContainer = true
    }

    protected open fun onOpenSearchField() {
        showTitleContainer = false
        showSearchIcon = false
        showUpNavigation = true

        menuExpansionContainer.layoutParams = (menuExpansionContainer.layoutParams as LayoutParams).apply {
            startToEnd = toolbarSpace.id
            width = LayoutParams.MATCH_CONSTRAINT
        }

        searchField.visibility = VISIBLE
        searchField.requestFocusAndOpenKeyboard()
    }

    private fun openSearchField() {
        if (!isSearchFieldExpanded) {
            onOpenSearchField()
        }
    }

    override fun upNavigate() {
        if (isSearchFieldExpanded) {
            closeSearchField()
        } else {
            super.upNavigate()
        }
    }

}
