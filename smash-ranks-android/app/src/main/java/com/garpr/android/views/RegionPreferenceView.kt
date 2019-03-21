package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.activities.SetRegionActivity
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.appComponent
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.RequestCodes
import javax.inject.Inject

class RegionPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), RegionManager.OnRegionChangeListener,
        View.OnClickListener {

    @Inject
    protected lateinit var regionManager: RegionManager

    init {
        titleText = resources.getText(R.string.region)

        if (isInEditMode) {
            descriptionText = resources.getString(R.string.region_endpoint_format,
                    resources.getString(R.string.norcal), resources.getString(R.string.gar_pr))
        }

        imageDrawable = ContextCompat.getDrawable(context, R.drawable.ic_location_on_white_24dp)

        setOnClickListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        regionManager.addListener(this)
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
        super.onDetachedFromWindow()

        regionManager.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
            regionManager.addListener(this)
            refresh()
        }
    }

    override fun onRegionChange(regionManager: RegionManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        val region = regionManager.getRegion(context)
        descriptionText = resources.getString(R.string.region_endpoint_format,
                region.displayName, resources.getString(region.endpoint.title))
    }

}
