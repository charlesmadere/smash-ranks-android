package com.garpr.android.extensions

import android.view.MenuItem
import com.garpr.android.R
import com.garpr.android.misc.HomeTab

val MenuItem.itemIdAsHomeTab: HomeTab
    get() = when (itemId) {
                R.id.actionFavoritePlayers -> HomeTab.FAVORITE_PLAYERS
                R.id.actionRankings -> HomeTab.RANKINGS
                R.id.actionTournaments -> HomeTab.TOURNAMENTS
                else -> throw RuntimeException("unknown item: $title")
            }
