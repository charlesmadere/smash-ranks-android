package com.garpr.android.features.common.views

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.garpr.android.features.common.adapters.BaseAdapterView
import kotlinx.android.synthetic.main.divider_string.view.*

class StringDividerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), BaseAdapterView<String> {

    override fun setContent(content: String) {
        textView.text = content
    }

}
