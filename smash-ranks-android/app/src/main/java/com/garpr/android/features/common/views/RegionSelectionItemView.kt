package com.garpr.android.features.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.garpr.android.data.models.Region
import com.garpr.android.features.common.adapters.BaseAdapterView
import kotlinx.android.synthetic.main.item_region_selection.view.*

class RegionSelectionItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), BaseAdapterView<Pair<Region, Boolean>>, View.OnClickListener {

    var onClickListener: OnClickListener? = null

    private var _region: Region? = null

    val region: Region
        get() = requireNotNull(_region)

    interface OnClickListener {
        fun onClick(v: RegionSelectionItemView)
    }

    init {
        setOnClickListener(this)
    }

    override fun onClick(v: View) {
        onClickListener?.onClick(this)
    }

    override fun setContent(content: Pair<Region, Boolean>) {
        _region = content.first
        displayName.text = content.first.displayName
        regionId.text = content.first.id
        radioButton.isChecked = content.second
    }

}
