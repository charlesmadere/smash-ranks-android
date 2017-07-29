package com.garpr.android.views.toolbars

import android.content.Context
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.misc.IdentityManager
import com.garpr.android.misc.RegionManager
import javax.inject.Inject

class HomeToolbar : SearchToolbar, IdentityManager.OnIdentityChangeListener,
        RegionManager.OnRegionChangeListener {

    @Inject
    lateinit protected var mIdentityManager: IdentityManager

    @Inject
    lateinit protected var mRegionManager: RegionManager


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        mIdentityManager.addListener(this)
        mRegionManager.addListener(this)
    }

    override fun onCreateOptionsMenu(inflater: MenuInflater, menu: Menu) {
        inflater.inflate(R.menu.toolbar_home, menu)
        super.onCreateOptionsMenu(inflater, menu)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mIdentityManager.removeListener(this)
        mRegionManager.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
            mIdentityManager.addListener(this)
            mRegionManager.addListener(this)
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

        val region = mRegionManager.getRegion(context)
        menu.findItem(R.id.miActivityRequirements).isVisible = region.hasActivityRequirements()
        menu.findItem(R.id.miViewYourself).isVisible = mIdentityManager.hasIdentity()
    }

    override fun onRegionChange(regionManager: RegionManager) {
        if (isAlive) {
            closeSearchLayout()
            postRefreshMenu()
        }
    }

}
