package com.garpr.android.features.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.appComponent
import com.garpr.android.features.common.views.SimplePreferenceView
import com.garpr.android.features.setRegion.SetRegionActivity
import com.garpr.android.misc.RequestCodes
import com.garpr.android.repositories.RegionRepository
import javax.inject.Inject

class RegionPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), RegionRepository.OnRegionChangeListener,
        View.OnClickListener {

    @Inject
    protected lateinit var regionRepository: RegionRepository


    init {
        titleText = context.getText(R.string.region)
        imageDrawable = ContextCompat.getDrawable(context, R.drawable.ic_location_on_white_24dp)
        setOnClickListener(this)

        if (isInEditMode) {
            descriptionText = context.getString(R.string.region_endpoint_format,
                    context.getString(R.string.norcal), context.getString(R.string.gar_pr))
        } else {
            appComponent.inject(this)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        regionRepository.addListener(this)
        refresh()
    }

    override fun onClick(v: View) {
        val activity = this.activity

        if (activity == null) {
            context.startActivity(SetRegionActivity.getLaunchIntent(context))
        } else {
            activity.startActivityForResult(SetRegionActivity.getLaunchIntent(activity),
                    RequestCodes.CHANGE_REGION.value)
        }
    }

    override fun onDetachedFromWindow() {
        regionRepository.removeListener(this)
        super.onDetachedFromWindow()
    }

    override fun onRegionChange(regionRepository: RegionRepository) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        val region = regionRepository.getRegion(context)
        descriptionText = context.getString(R.string.region_endpoint_format,
                region.displayName, context.getString(region.endpoint.title))
    }

}
