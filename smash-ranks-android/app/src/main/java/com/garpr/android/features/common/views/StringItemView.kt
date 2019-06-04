package com.garpr.android.features.common.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.garpr.android.features.common.adapters.BaseAdapterView

class StringItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs), BaseAdapterView<String> {

    override fun setContent(content: String) {
        text = content
    }

}
