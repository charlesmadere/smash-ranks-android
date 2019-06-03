package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.garpr.android.features.base.BaseAdapterView
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.clear
import kotlinx.android.synthetic.main.item_region_selection.view.*

class RegionSelectionItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), BaseAdapterView<Region>, View.OnClickListener {

    interface Listeners {
        fun onClick(v: RegionSelectionItemView)
        val selectedRegion: Region?
    }

    private fun clear() {
        radioButton.isChecked = false
        displayName.clear()
        regionId.clear()
    }

    override fun onClick(v: View) {
        (activity as? Listeners?)?.onClick(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setOnClickListener(this)
    }

    var region: Region? = null
        private set(value) {
            field = value

            if (value == null) {
                clear()
                return
            }

            radioButton.isChecked = value == (activity as? Listeners?)?.selectedRegion
            displayName.text = value.displayName
            regionId.text = value.id
        }

    override fun setContent(content: Region) {
        region = content
    }

}
