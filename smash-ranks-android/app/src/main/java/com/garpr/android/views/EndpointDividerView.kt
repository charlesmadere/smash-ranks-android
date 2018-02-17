package com.garpr.android.views

import android.content.Context
import android.support.annotation.AttrRes
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.models.Endpoint
import kotterknife.bindView

class EndpointDividerView(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), BaseAdapterView<Endpoint> {

    private val name: TextView by bindView(R.id.tvName)


    override fun setContent(content: Endpoint) {
        name.text = resources.getText(content.title)
    }

}
