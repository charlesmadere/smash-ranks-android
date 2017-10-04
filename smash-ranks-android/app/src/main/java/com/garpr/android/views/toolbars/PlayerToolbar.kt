package com.garpr.android.views.toolbars

import android.content.Context
import android.support.annotation.AttrRes
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
import com.garpr.android.models.Match
import com.garpr.android.models.MatchesBundle
import javax.inject.Inject

class PlayerToolbar : SearchToolbar, FavoritePlayersManager.OnFavoritePlayersChangeListener,
        IdentityManager.OnIdentityChangeListener {

    @Inject
    lateinit protected var mFavoritePlayersManager: FavoritePlayersManager

    @Inject
    lateinit protected var mIdentityManager: IdentityManager

    @Inject
    lateinit protected var mPlayerToolbarManager: PlayerToolbarManager


    interface DataProvider {
        val fullPlayer: FullPlayer?
        val matchesBundle: MatchesBundle?
        val matchResult: MatchResult?
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        mFavoritePlayersManager.addListener(this)
        mIdentityManager.addListener(this)
    }

    override fun onCreateOptionsMenu(inflater: MenuInflater, menu: Menu) {
        inflater.inflate(R.menu.toolbar_player, menu)
        super.onCreateOptionsMenu(inflater, menu)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mFavoritePlayersManager.removeListener(this)
        mIdentityManager.removeListener(this)
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
            mFavoritePlayersManager.addListener(this)
            mIdentityManager.addListener(this)
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
        val matchResult: Match.Result?

        if (activity is DataProvider) {
            fullPlayer = activity.fullPlayer
            matchesBundle = activity.matchesBundle
            matchResult = activity.result
        } else {
            fullPlayer = null
            matchesBundle = null
            matchResult = null
        }

        val presentation = mPlayerToolbarManager.getPresentation(fullPlayer, matchesBundle,
                matchResult)

        menu.findItem(R.id.miAddToFavorites).isVisible = presentation.mIsAddToFavoritesVisible
        menu.findItem(R.id.miAliases).isVisible = presentation.mIsAliasesVisible
        menu.findItem(R.id.miFilter).isVisible = presentation.mIsFilterVisible
        menu.findItem(R.id.miShowAll).isVisible = presentation.mIsFilterAllVisible
        menu.findItem(R.id.miFilterToLosses).isVisible = presentation.mIsFilterLossesVisible
        menu.findItem(R.id.miFilterToWins).isVisible = presentation.mIsFilterWinsVisible
        menu.findItem(R.id.miRemoveFromFavorites).isVisible = presentation.mIsRemoveFromFavoritesVisible
        menu.findItem(R.id.miShare).isVisible = presentation.mIsShareVisible

        val setAsYourIdentity = menu.findItem(R.id.miSetAsYourIdentity)
        setAsYourIdentity.isVisible = presentation.mIsSetAsYourIdentityVisible

        val viewYourselfVsThisOpponent = menu.findItem(R.id.miViewYourselfVsThisOpponent)
        viewYourselfVsThisOpponent.isVisible = presentation.mIsViewYourselfVsThisOpponentVisible
    }

}
