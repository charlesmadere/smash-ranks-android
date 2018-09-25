package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.models.Endpoint
import kotterknife.bindView

class EndpointDividerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), BaseAdapterView<Endpoint> {

    private val name: TextView by bindView(R.id.tvName)


    override fun setContent(content: Endpoint) {
        name.text = resources.getText(content.title)
    }

}
