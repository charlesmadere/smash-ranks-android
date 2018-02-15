package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import com.garpr.android.App
import com.garpr.android.extensions.optActivity
import com.garpr.android.extensions.smoothScrollToTop
import com.garpr.android.misc.ListLayout
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import javax.inject.Inject

abstract class SearchableRefreshLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : RefreshLayout(context, attrs), ListLayout, Searchable, SearchQueryHandle {

    @Inject
    protected lateinit var threadUtils: ThreadUtils


    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        App.get().appComponent.inject(this)
    }

    override val searchQuery: CharSequence?
        get() = (context.optActivity() as? SearchQueryHandle)?.searchQuery

    override fun smoothScrollToTop() {
        recyclerView?.smoothScrollToTop()
    }

}
