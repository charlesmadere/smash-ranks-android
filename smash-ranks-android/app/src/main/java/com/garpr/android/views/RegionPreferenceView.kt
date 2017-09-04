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
    lateinit protected var mRegionManager: RegionManager


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

        mRegionManager.addListener(this)
        refresh()
    }

    override fun onClick(v: View) {
        context.startActivity(Intent(context, SetRegionActivity::class.java))
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mRegionManager.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
        }

        setOnClickListener(this)
        titleText = resources.getText(R.string.set_your_region)

        if (isInEditMode) {
            return
        }

        mRegionManager.addListener(this)
        refresh()
    }

    override fun onRegionChange(regionManager: RegionManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        descriptionText = mRegionManager.getRegion().displayName
    }

}
