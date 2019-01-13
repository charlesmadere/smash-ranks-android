package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.data.models.Endpoint
import kotlinx.android.synthetic.main.divider_endpoint.view.*

class EndpointDividerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), BaseAdapterView<Endpoint> {

    override fun setContent(content: Endpoint) {
        name.text = resources.getText(content.title)
    }

}
