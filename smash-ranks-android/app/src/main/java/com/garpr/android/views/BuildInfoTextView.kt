package com.garpr.android.views

import android.content.Context
import android.support.annotation.AttrRes
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

import com.garpr.android.BuildConfig
import com.garpr.android.R

class BuildInfoTextView : AppCompatTextView {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        text = resources.getString(R.string.build_info_format, BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE)
    }

}
