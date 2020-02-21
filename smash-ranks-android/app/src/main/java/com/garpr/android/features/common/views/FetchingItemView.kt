package com.garpr.android.features.common.views

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.item_fetching.view.*

class FetchingItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    fun setContent(content: CharSequence) {
        textView.text = content
    }

}
