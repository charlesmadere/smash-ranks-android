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
        var presentation = Presentation()

        if (fullPlayer == null) {
            return presentation
        }

        presentation = if (fullPlayer in favoritePlayersManager) {
            presentation.copy(isRemoveFromFavoritesVisible = true)
        } else {
            presentation.copy(isAddToFavoritesVisible = true)
        }

        presentation = presentation.copy(isAliasesVisible = fullPlayer.aliases?.isNotEmpty() == true)

        if (matchesBundle?.matches?.isNotEmpty() == true) {
            presentation = presentation.copy(
                    isFilterVisible = true,
                    isFilterAllVisible = matchResult != null,
                    isFilterLossesVisible = matchResult != MatchResult.LOSE,
                    isFilterWinsVisible = matchResult != MatchResult.WIN
            )
        }

        presentation = presentation.copy(isShareVisible = true)

        if (identityManager.hasIdentity) {
            if (!identityManager.isPlayer(fullPlayer)) {
                presentation = presentation.copy(isViewYourselfVsThisOpponentVisible = true)
            }
        } else {
            presentation = presentation.copy(isSetAsYourIdentityVisible = true)
        }

        return presentation
    }

}
