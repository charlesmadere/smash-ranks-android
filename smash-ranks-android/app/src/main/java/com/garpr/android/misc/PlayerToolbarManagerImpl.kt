package com.garpr.android.misc

import android.content.res.Resources
import com.garpr.android.R
import com.garpr.android.misc.PlayerToolbarManager.Presentation
import com.garpr.android.models.FullPlayer
import com.garpr.android.models.Match
import com.garpr.android.models.MatchesBundle

class PlayerToolbarManagerImpl(
        val mFavoritePlayersManager: FavoritePlayersManager,
        val mIdentityManager: IdentityManager
) : PlayerToolbarManager {

    override fun getPresentation(resources: Resources, fullPlayer: FullPlayer?,
            matchesBundle: MatchesBundle?, matchResult: Match.Result?): Presentation {
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
            presentation.mIsFilterLossesVisible = matchResult != Match.Result.LOSE
            presentation.mIsFilterWinsVisible = matchResult != Match.Result.WIN
        }

        presentation.mIsShareVisible = true

        if (mIdentityManager.hasIdentity) {
            if (!mIdentityManager.isPlayer(fullPlayer)) {
                presentation.mViewYourselfVsThisOpponentTitle = resources.getString(
                        R.string.view_yourself_vs_x, fullPlayer.name)
                presentation.mIsViewYourselfVsThisOpponentVisible = true
            }
        } else {
            presentation.mSetAsYourIdentityTitle = resources.getString(
                    R.string.set_x_as_your_identity, fullPlayer.name)
            presentation.mIsSetAsYourIdentityVisible = true
        }

        return presentation
    }

}
