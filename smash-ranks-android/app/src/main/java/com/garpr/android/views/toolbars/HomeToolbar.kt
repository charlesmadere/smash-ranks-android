package com.garpr.android.views.toolbars

import android.content.Context
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.misc.HomeToolbarManager
import com.garpr.android.misc.IdentityManager
import com.garpr.android.misc.RegionManager
import javax.inject.Inject

class HomeToolbar : SearchToolbar, IdentityManager.OnIdentityChangeListener,
        RegionManager.OnRegionChangeListener {

    @Inject
    protected lateinit var homeToolbarManager: HomeToolbarManager

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var regionManager: RegionManager


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        identityManager.addListener(this)
        regionManager.addListener(this)
    }

    override fun onCreateOptionsMenu(inflater: MenuInflater, menu: Menu) {
        inflater.inflate(R.menu.toolbar_home, menu)
        super.onCreateOptionsMenu(inflater, menu)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        identityManager.removeListener(this)
        regionManager.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
            identityManager.addListener(this)
            regionManager.addListener(this)
        }
    }

    override fun onIdentityChange(identityManager: IdentityManager) {
        if (isAlive) {
            postRefreshMenu()
        }
    }

    override fun onRefreshMenu() {
        super.onRefreshMenu()

        if (isSearchLayoutExpanded) {
            return
        }

        val presentation = homeToolbarManager.getPresentation(context)
        menu.findItem(R.id.miActivityRequirements).isVisible = presentation.mIsActivityRequirementsVisible
        menu.findItem(R.id.miViewYourself).isVisible = presentation.mIsViewYourselfVisible
    }

    override fun onRegionChange(regionManager: RegionManager) {
        if (isAlive) {
            closeSearchLayout()
            postRefreshMenu()
        }
    }

}
