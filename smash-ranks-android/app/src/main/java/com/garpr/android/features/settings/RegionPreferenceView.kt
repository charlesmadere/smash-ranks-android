package com.garpr.android.features.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.data.models.Region
import com.garpr.android.features.common.views.SimplePreferenceView

class RegionPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), View.OnClickListener {

    var listener: Listener? = null

    interface Listener {
        fun onClick(v: RegionPreferenceView)
    }

    init {
        titleText = context.getText(R.string.region)
        imageDrawable = ContextCompat.getDrawable(context, R.drawable.ic_location_on_white_24dp)
        setOnClickListener(this)

        if (isInEditMode) {
            descriptionText = context.getString(R.string.region_endpoint_format,
                    context.getString(R.string.norcal), context.getString(R.string.gar_pr))
        }
    }

    override fun onClick(v: View) {
        listener?.onClick(this)
    }

    fun setContent(region: Region) {
        descriptionText = context.getString(R.string.region_endpoint_format,
                region.displayName, context.getString(region.endpoint.title))
    }

}
