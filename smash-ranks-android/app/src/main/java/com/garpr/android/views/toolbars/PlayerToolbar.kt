package com.garpr.android.views.toolbars

import android.content.Context
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.misc.FavoritePlayersManager
import com.garpr.android.misc.IdentityManager
import com.garpr.android.misc.MiscUtils
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


    interface DataProvider {
        val fullPlayer: FullPlayer?
        val matchesBundle: MatchesBundle?
        val result: Match.Result?
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
            attrs, defStyleAttr)

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

        val activity = MiscUtils.optActivity(context)
        val fullPlayer: FullPlayer?
        val matchesBundle: MatchesBundle?
        val result: Match.Result?

        if (activity is DataProvider) {
            fullPlayer = (activity as DataProvider).fullPlayer
            matchesBundle = (activity as DataProvider).matchesBundle
            result = (activity as DataProvider).result
        } else {
            fullPlayer = null
            matchesBundle = null
            result = null
        }

        if (fullPlayer == null) {
            return
        }

        if (matchesBundle != null && matchesBundle.hasMatches()) {
            menu.findItem(R.id.miFilter).isVisible = true
            menu.findItem(R.id.miFilterAll).isVisible = result != null
            menu.findItem(R.id.miFilterLosses).isVisible = result != Match.Result.LOSE
            menu.findItem(R.id.miFilterWins).isVisible = result != Match.Result.WIN
        }

        menu.findItem(R.id.miShare).isVisible = true

        if (mFavoritePlayersManager.containsPlayer(fullPlayer)) {
            menu.findItem(R.id.miRemoveFromFavorites).isVisible = true
        } else {
            menu.findItem(R.id.miAddToFavorites).isVisible = true
        }

        if (fullPlayer.hasAliases()) {
            menu.findItem(R.id.miAliases).isVisible = true
        }

        if (mIdentityManager.hasIdentity() && !mIdentityManager.isPlayer(fullPlayer)) {
            val menuItem = menu.findItem(R.id.miViewYourselfVsThisOpponent)
            menuItem.title = resources.getString(R.string.view_yourself_vs_x, fullPlayer.name)
            menuItem.isVisible = true
        }
    }

}
