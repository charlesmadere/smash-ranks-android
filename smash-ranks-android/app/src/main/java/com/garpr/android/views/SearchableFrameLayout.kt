package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.appComponent
import com.garpr.android.misc.ListLayout
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import javax.inject.Inject

abstract class SearchableFrameLayout : LifecycleFrameLayout, ListLayout, Searchable,
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

        appComponent.inject(this)
    }

    override val searchQuery: CharSequence?
        get() = (activity as? SearchQueryHandle)?.searchQuery

}
