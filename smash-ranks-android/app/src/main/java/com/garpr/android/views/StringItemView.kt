package com.garpr.android.views

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.garpr.android.adapters.BaseAdapterView

class StringItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs), BaseAdapterView<String> {

    override fun setContent(content: String) {
        text = content
    }

}
