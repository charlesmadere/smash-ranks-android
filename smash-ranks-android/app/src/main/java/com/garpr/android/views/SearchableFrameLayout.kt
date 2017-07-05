package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import com.garpr.android.App
import com.garpr.android.misc.ListLayout
import com.garpr.android.misc.MiscUtils
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import javax.inject.Inject

abstract class SearchableFrameLayout : LifecycleFrameLayout, ListLayout, Refreshable, Searchable,
        SearchQueryHandle {

    @Inject
    lateinit protected var mThreadUtils: ThreadUtils


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

        App.sInstance.mAppComponent.inject(this)
    }

    override fun scrollToTop() {
        recyclerView?.let {
            val layoutManager = it.layoutManager

            if (layoutManager == null) {
                it.scrollToPosition(0)
            } else {
                layoutManager.smoothScrollToPosition(it, RecyclerView.State(), 0)
            }
        }
    }

    override val searchQuery: CharSequence?
        get() {
            val activity = MiscUtils.optActivity(context)

            if (activity is SearchQueryHandle) {
                return activity.searchQuery
            } else {
                return null
            }
        }

}
