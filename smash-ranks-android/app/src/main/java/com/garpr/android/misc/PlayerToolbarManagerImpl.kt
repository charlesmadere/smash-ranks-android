package com.garpr.android.misc

import com.garpr.android.misc.PlayerToolbarManager.Presentation
import com.garpr.android.models.FullPlayer
import com.garpr.android.models.MatchResult
import com.garpr.android.models.MatchesBundle

class PlayerToolbarManagerImpl(
        val mFavoritePlayersManager: FavoritePlayersManager,
        val mIdentityManager: IdentityManager
) : PlayerToolbarManager {

    override fun getPresentation(fullPlayer: FullPlayer?, matchesBundle: MatchesBundle?,
            matchResult: MatchResult?): Presentation {
        val presentation = Presentation()

        if (fullPlayer == null) {
            return presentation
        }

        if (fullPlayer in mFavoritePlayersManager) {
            presentation.mIsRemoveFromFavoritesVisible = true
        } else {
            presentation.mIsAddToFavoritesVisible = true
        }

        presentation.mIsAliasesVisible = fullPlayer.aliases?.isNotEmpty() == true

        if (matchesBundle?.matches?.isNotEmpty() == true) {
            presentation.mIsFilterVisible = true
            presentation.mIsFilterAllVisible = matchResult != null
            presentation.mIsFilterLossesVisible = matchResult != MatchResult.LOSE
            presentation.mIsFilterWinsVisible = matchResult != MatchResult.WIN
        }

        presentation.mIsShareVisible = true

        if (mIdentityManager.hasIdentity) {
            if (!mIdentityManager.isPlayer(fullPlayer)) {
                presentation.mIsViewYourselfVsThisOpponentVisible = true
            }
        } else {
            presentation.mIsSetAsYourIdentityVisible = true
        }

        return presentation
    }

}
