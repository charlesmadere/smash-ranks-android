package com.garpr.android.views

import android.content.Context
import android.support.annotation.AttrRes
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

import com.garpr.android.adapters.BaseAdapterView

class StringItemView : AppCompatTextView, BaseAdapterView<String> {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override fun setContent(content: String) {
        text = content
    }

}
