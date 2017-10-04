package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.View
import com.garpr.android.App
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.models.HeadToHeadMatch

class HeadToHeadItemView : IdentityFrameLayout, BaseAdapterView<HeadToHeadMatch>,
        View.OnClickListener {

    private val mContent: HeadToHeadMatch? = null


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onClick(v: View) {

    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
        }


    }

    override fun setContent(content: HeadToHeadMatch) {
        // TODO
    }

}
