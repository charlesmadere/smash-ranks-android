package com.garpr.android.features.common

import android.content.Context
import android.util.AttributeSet
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.appComponent
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import javax.inject.Inject

abstract class SearchableRefreshLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : RefreshLayout(context, attrs), Searchable, SearchQueryHandle {

    @Inject
    protected lateinit var threadUtils: ThreadUtils


    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        appComponent.inject(this)
    }

    override val searchQuery: CharSequence?
        get() = (activity as? SearchQueryHandle?)?.searchQuery

}
