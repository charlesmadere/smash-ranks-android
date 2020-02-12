package com.garpr.android.features.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.garpr.android.data.models.Region
import kotlinx.android.synthetic.main.item_region_selection.view.*

class RegionSelectionItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), View.OnClickListener {

    var listener: Listener? = null

    private var _region: Region? = null

    val region: Region
        get() = checkNotNull(_region)

    init {
        setOnClickListener(this)
    }

    override fun onClick(v: View) {
        listener?.onClick(this)
    }

    fun setContent(region: Region, isChecked: Boolean) {
        _region = region
        regionDisplayName.text = region.displayName
        regionId.text = region.id
        radioButton.isChecked = isChecked
    }

    interface Listener {
        fun onClick(v: RegionSelectionItemView)
    }

}
