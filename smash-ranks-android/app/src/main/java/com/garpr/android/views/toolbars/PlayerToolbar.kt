package com.garpr.android.views.toolbars

import android.content.Context
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import com.garpr.android.R
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.optActivity
import com.garpr.android.managers.FavoritePlayersManager
import com.garpr.android.managers.IdentityManager
import com.garpr.android.managers.PlayerToolbarManager
import com.garpr.android.models.MatchResult
import com.garpr.android.models.MatchesBundle
import javax.inject.Inject

class PlayerToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SearchToolbar(context, attrs), FavoritePlayersManager.OnFavoritePlayersChangeListener,
        IdentityManager.OnIdentityChangeListener {

    @Inject
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var playerToolbarManager: PlayerToolbarManager


    interface DataProvider {
        val matchesBundle: MatchesBundle?
        val matchResult: MatchResult?
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        favoritePlayersManager.addListener(this)
        identityManager.addListener(this)
    }

    override fun onCreateOptionsMenu(inflater: MenuInflater, menu: Menu) {
        inflater.inflate(R.menu.toolbar_player, menu)
        super.onCreateOptionsMenu(inflater, menu)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        favoritePlayersManager.removeListener(this)
        identityManager.removeListener(this)
    }

    override fun onFavoritePlayersChange(favoritePlayersManager: FavoritePlayersManager) {
        if (isAlive) {
            postRefresh()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
            favoritePlayersManager.addListener(this)
            identityManager.addListener(this)
        }
    }

    override fun onIdentityChange(identityManager: IdentityManager) {
        if (isAlive) {
            postRefresh()
        }
    }

    override fun onRefreshMenu() {
        super.onRefreshMenu()

        if (isSearchLayoutExpanded) {
            return
        }

        val activity = context.optActivity()
        val matchesBundle: MatchesBundle?
        val matchResult: MatchResult?

        if (activity is DataProvider) {
            matchesBundle = activity.matchesBundle
            matchResult = activity.matchResult
        } else {
            matchesBundle = null
            matchResult = null
        }

        val presentation = playerToolbarManager.getPresentation(matchesBundle, matchResult)
        menu.findItem(R.id.miFilter).isVisible = presentation.isFilterVisible
        menu.findItem(R.id.miShowAll).isVisible = presentation.isFilterAllVisible
        menu.findItem(R.id.miFilterToLosses).isVisible = presentation.isFilterLossesVisible
        menu.findItem(R.id.miFilterToWins).isVisible = presentation.isFilterWinsVisible
    }

}
