package com.garpr.android.misc

import com.garpr.android.misc.PlayerToolbarManager.Presentation
import com.garpr.android.models.FullPlayer
import com.garpr.android.models.MatchResult
import com.garpr.android.models.MatchesBundle

class PlayerToolbarManagerImpl(
        private val favoritePlayersManager: FavoritePlayersManager,
        private val identityManager: IdentityManager
) : PlayerToolbarManager {

    override fun getPresentation(fullPlayer: FullPlayer?, matchesBundle: MatchesBundle?,
            matchResult: MatchResult?): Presentation {
        val presentation = Presentation()

        if (fullPlayer == null) {
            return presentation
        }

        if (fullPlayer in favoritePlayersManager) {
            presentation.isRemoveFromFavoritesVisible = true
        } else {
            presentation.isAddToFavoritesVisible = true
        }

        presentation.isAliasesVisible = fullPlayer.aliases?.isNotEmpty() == true

        if (matchesBundle?.matches?.isNotEmpty() == true) {
            presentation.isFilterVisible = true
            presentation.isFilterAllVisible = matchResult != null
            presentation.isFilterLossesVisible = matchResult != MatchResult.LOSE
            presentation.isFilterWinsVisible = matchResult != MatchResult.WIN
        }

        presentation.isShareVisible = true

        if (identityManager.hasIdentity) {
            if (!identityManager.isPlayer(fullPlayer)) {
                presentation.isViewYourselfVsThisOpponentVisible = true
            }
        } else {
            presentation.isSetAsYourIdentityVisible = true
        }

        return presentation
    }

}
