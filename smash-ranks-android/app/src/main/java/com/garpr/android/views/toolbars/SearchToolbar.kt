package com.garpr.android.views.toolbars

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.garpr.android.extensions.activity
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable

open class SearchToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : GarToolbar(context, attrs), SearchQueryHandle, TextView.OnEditorActionListener, TextWatcher {

    interface Listener {
        val showSearchIcon: Boolean
    }

    override fun afterTextChanged(s: Editable?) {
        (activity as? Searchable)?.search(s?.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // intentionally empty
    }

    fun closeSearchField() {
        if (isSearchFieldExpanded) {
            TODO()
        }
    }

    val isSearchFieldExpanded: Boolean
        get() = TODO()

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            (activity as? Searchable)?.search(v.text?.toString())
        }

        return false
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // intentionally empty
    }

    override fun refresh() {
        super.refresh()
        closeSearchField()

        if ((activity as? Listener)?.showSearchIcon == true) {

        } else {

        }
    }

    override val searchQuery: CharSequence?
        get() = TODO()

    override fun upNavigate() {
        if (isSearchFieldExpanded) {
            closeSearchField()
        } else {
            super.upNavigate()
        }
    }

}
