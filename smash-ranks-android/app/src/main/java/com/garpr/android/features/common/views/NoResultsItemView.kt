package com.garpr.android.features.common.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.garpr.android.R

class NoResultsItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : AppCompatTextView(context,  attrs) {

    fun setContent(content: String) {
        text = resources.getString(R.string.no_search_results_for_x, content)
    }

}
