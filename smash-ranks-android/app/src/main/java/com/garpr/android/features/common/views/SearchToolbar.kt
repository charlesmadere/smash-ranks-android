package com.garpr.android.features.common.views

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.View
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
        get() = searchField.visibility == View.VISIBLE

    override val searchQuery: CharSequence?
        get() = searchField.text

    var showSearchIcon: Boolean
        get() = searchIcon.visibility == View.VISIBLE
        set(value) {
            searchIcon.visibility = if (!searchField.isFocused && value) View.VISIBLE else View.GONE
        }

    private val wasShowingUpNavigation = showUpNavigation

    private val searchIconClickListener = OnClickListener {
        openSearchField()
    }

    private val searchFieldActionListener = TextView.OnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            (activity as? Searchable?)?.search(searchQuery?.toString())
            activity?.hideKeyboard()
        }

        false
    }

    private val searchFieldTextWatcher = object : AbstractTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            (activity as? Searchable?)?.search(searchQuery?.toString())
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
        searchField.visibility = View.GONE
        showUpNavigation = wasShowingUpNavigation

        menuExpansionContainer.layoutParams = (menuExpansionContainer.layoutParams as LayoutParams).apply {
            startToEnd = View.NO_ID
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

        searchField.visibility = View.VISIBLE
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
