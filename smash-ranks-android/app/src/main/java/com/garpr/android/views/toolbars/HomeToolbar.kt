package com.garpr.android.views.toolbars

import android.content.Context
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.core.widget.PopupMenuCompat
import com.garpr.android.R
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.managers.HomeToolbarManager
import com.garpr.android.managers.IdentityManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.RankingCriteriaHandle
import kotlinx.android.synthetic.main.gar_toolbar.view.*
import kotlinx.android.synthetic.main.home_toolbar_items.view.*
import javax.inject.Inject

class HomeToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SearchToolbar(context, attrs), IdentityManager.OnIdentityChangeListener,
        RegionManager.OnRegionChangeListener {

    private val overflowPopupMenu: PopupMenu

    @Inject
    protected lateinit var homeToolbarManager: HomeToolbarManager

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var regionManager: RegionManager


    interface Listeners {
        fun onActivityRequirementsClick(v: HomeToolbar)
        fun onSettingsClick(v: HomeToolbar)
        fun onShareClick(v: HomeToolbar)
        fun onViewAllPlayersClick(v: HomeToolbar)
        fun onViewYourselfClick(v: HomeToolbar)
    }

    init {
        layoutInflater.inflate(R.layout.home_toolbar_items, menuExpansionContainer)

        overflowPopupMenu = PopupMenu(context, overflowButton)
        overflowButton.setOnTouchListener(PopupMenuCompat.getDragToOpenListener(overflowPopupMenu))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        identityManager.addListener(this)
        regionManager.addListener(this)
    }

    override fun onCloseSearchField() {
        super.onCloseSearchField()
        overflowButton.visibility = View.VISIBLE
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

        overflowButton.setOnClickListener {
            overflowPopupMenu.show()
        }
    }

    override fun onIdentityChange(identityManager: IdentityManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun onOpenSearchField() {
        super.onOpenSearchField()
        overflowButton.visibility = View.GONE
    }

    override fun onRegionChange(regionManager: RegionManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        val presentation = homeToolbarManager.getPresentation(
                (activity as? RankingCriteriaHandle)?.rankingCriteria)

        overflowPopupMenu.menu.clear()
        overflowPopupMenu.menu.add(R.string.share)
                .setOnMenuItemClickListener(shareClickListener)

        if (presentation.isActivityRequirementsVisible) {
            overflowPopupMenu.menu.add(R.string.activity_requirements)
                    .setOnMenuItemClickListener(activityRequirementsClickListener)
        }

        overflowPopupMenu.menu.add(R.string.view_all_players)
                .setOnMenuItemClickListener(viewAllPlayersClickListener)

        if (presentation.isViewYourselfVisible) {
            overflowPopupMenu.menu.add(R.string.view_yourself)
                    .setOnMenuItemClickListener(viewYourselfClickListener)
        }

        overflowPopupMenu.menu.add(R.string.settings)
                .setOnMenuItemClickListener(settingsClickListener)
    }

    private val activityRequirementsClickListener = MenuItem.OnMenuItemClickListener {
        (activity as? Listeners)?.onActivityRequirementsClick(this)
        true
    }

    private val shareClickListener = MenuItem.OnMenuItemClickListener {
        (activity as? Listeners)?.onShareClick(this)
        true
    }

    private val viewAllPlayersClickListener = MenuItem.OnMenuItemClickListener {
        (activity as? Listeners)?.onViewAllPlayersClick(this)
        true
    }

    private val viewYourselfClickListener = MenuItem.OnMenuItemClickListener {
        (activity as? Listeners)?.onViewYourselfClick(this)
        true
    }

    private val settingsClickListener = MenuItem.OnMenuItemClickListener {
        (activity as? Listeners)?.onSettingsClick(this)
        true
    }

}
