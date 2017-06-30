package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import com.garpr.android.App
import com.garpr.android.misc.MiscUtils
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import javax.inject.Inject

abstract class SearchableFrameLayout : LifecycleFrameLayout, Searchable, SearchQueryHandle {

    @Inject
    lateinit protected var mThreadUtils: ThreadUtils


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun getSearchQuery(): CharSequence? {
        val activity = MiscUtils.optActivity(context)

        if (activity is SearchQueryHandle) {
            return activity.searchQuery
        } else {
            return null
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        App.get().appComponent.inject(this)
    }

}
