package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.View
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.activities.SetRegionActivity
import com.garpr.android.misc.RegionManager
import javax.inject.Inject

class RegionPreferenceView : SimplePreferenceView, RegionManager.OnRegionChangeListener,
        View.OnClickListener {

    @Inject
    protected lateinit var regionManager: RegionManager


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        regionManager.addListener(this)
        refresh()
    }

    override fun onClick(v: View) {
        context.startActivity(Intent(context, SetRegionActivity::class.java))
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        regionManager.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
            regionManager.addListener(this)
        }

        setOnClickListener(this)
        titleText = resources.getText(R.string.region)

        if (isInEditMode) {
            descriptionText = resources.getString(R.string.region_endpoint_format,
                    resources.getString(R.string.norcal), resources.getString(R.string.gar_pr))
        } else {
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
