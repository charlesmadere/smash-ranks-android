package com.garpr.android.views.toolbars

import android.content.Context
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.extensions.optActivity
import com.garpr.android.misc.FavoritePlayersManager
import com.garpr.android.misc.IdentityManager
import com.garpr.android.misc.PlayerToolbarManager
import com.garpr.android.models.FullPlayer
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
        val fullPlayer: FullPlayer?
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

    override fun onFavoritePlayersChanged(manager: FavoritePlayersManager) {
        if (isAlive) {
            postRefreshMenu()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
            favoritePlayersManager.addListener(this)
            identityManager.addListener(this)
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

        val activity = context.optActivity()
        val fullPlayer: FullPlayer?
        val matchesBundle: MatchesBundle?
        val matchResult: MatchResult?

        if (activity is DataProvider) {
            fullPlayer = activity.fullPlayer
            matchesBundle = activity.matchesBundle
            matchResult = activity.matchResult
        } else {
            fullPlayer = null
            matchesBundle = null
            matchResult = null
        }

        val presentation = playerToolbarManager.getPresentation(fullPlayer, matchesBundle,
                matchResult)

        menu.findItem(R.id.miAddToFavorites).isVisible = presentation.isAddToFavoritesVisible
        menu.findItem(R.id.miAliases).isVisible = presentation.isAliasesVisible
        menu.findItem(R.id.miFilter).isVisible = presentation.isFilterVisible
        menu.findItem(R.id.miShowAll).isVisible = presentation.isFilterAllVisible
        menu.findItem(R.id.miFilterToLosses).isVisible = presentation.isFilterLossesVisible
        menu.findItem(R.id.miFilterToWins).isVisible = presentation.isFilterWinsVisible
        menu.findItem(R.id.miRemoveFromFavorites).isVisible = presentation.isRemoveFromFavoritesVisible
        menu.findItem(R.id.miShare).isVisible = presentation.isShareVisible

        val setAsYourIdentity = menu.findItem(R.id.miSetAsYourIdentity)
        setAsYourIdentity.isVisible = presentation.isSetAsYourIdentityVisible

        val viewYourselfVsThisOpponent = menu.findItem(R.id.miViewYourselfVsThisOpponent)
        viewYourselfVsThisOpponent.isVisible = presentation.isViewYourselfVsThisOpponentVisible
    }

}
