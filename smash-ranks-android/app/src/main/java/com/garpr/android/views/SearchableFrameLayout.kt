package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import com.garpr.android.App
import com.garpr.android.extensions.optActivity
import com.garpr.android.extensions.smoothScrollToTop
import com.garpr.android.misc.*
import javax.inject.Inject

abstract class SearchableFrameLayout : LifecycleFrameLayout, ListLayout, Refreshable, Searchable,
        SearchQueryHandle {

    @Inject
    protected lateinit var threadUtils: ThreadUtils


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        App.get().appComponent.inject(this)
    }

    override fun smoothScrollToTop() {
        recyclerView?.smoothScrollToTop()
    }

    override val searchQuery: CharSequence?
        get() {
            val activity = context.optActivity()

            return if (activity is SearchQueryHandle) {
                activity.searchQuery
            } else {
                null
            }
        }

}
