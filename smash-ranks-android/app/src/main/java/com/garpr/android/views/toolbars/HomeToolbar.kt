package com.garpr.android.views.toolbars

import android.content.Context
import android.util.AttributeSet
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.appComponent
import com.garpr.android.managers.HomeToolbarManager
import com.garpr.android.managers.IdentityManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.RankingCriteriaHandle
import javax.inject.Inject

class HomeToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SearchToolbar(context, attrs), IdentityManager.OnIdentityChangeListener,
        RegionManager.OnRegionChangeListener {

    @Inject
    protected lateinit var homeToolbarManager: HomeToolbarManager

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var regionManager: RegionManager


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        identityManager.addListener(this)
        regionManager.addListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        identityManager.removeListener(this)
        regionManager.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
            identityManager.addListener(this)
            regionManager.addListener(this)
        }
    }

    override fun onIdentityChange(identityManager: IdentityManager) {
        if (isAlive) {
            closeSearchField()
            refresh()
        }
    }

    override fun onRegionChange(regionManager: RegionManager) {
        if (isAlive) {
            closeSearchField()
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        val presentation = homeToolbarManager.getPresentation(
                (activity as? RankingCriteriaHandle)?.rankingCriteria)

        TODO()

        if (presentation.isActivityRequirementsVisible) {

        } else {

        }

        if (presentation.isViewYourselfVisible) {

        } else {

        }
    }

}
