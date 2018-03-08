package com.garpr.android.misc

import android.app.Application
import android.text.TextUtils
import com.garpr.android.R
import com.garpr.android.misc.PlayerProfileManager.Presentation
import com.garpr.android.models.AbsRegion
import com.garpr.android.models.FullPlayer

class PlayerProfileManagerImpl(
        val application: Application,
        val favoritePlayersManager: FavoritePlayersManager,
        val identityManager: IdentityManager,
        val regionManager: RegionManager
) : PlayerProfileManager {

    override fun getPresentation(player: FullPlayer, region: AbsRegion): Presentation {
        var presentation = Presentation(
                isAddToFavoritesVisible = !favoritePlayersManager.contains(player),
                isViewYourselfVsThisOpponentVisible = identityManager.hasIdentity &&
                        !identityManager.isPlayer(player))

        val uniqueAliases = player.uniqueAliases
        if (uniqueAliases?.isNotEmpty() == true) {
            presentation = presentation.copy(aliases = application.getString(R.string.aliases_x,
                    TextUtils.join(application.getText(R.string.delimiter), uniqueAliases)))
        }

        val rating = player.ratings?.get(region.id)
        if (rating != null) {
            presentation = presentation.copy(
                    rating = application.getString(R.string.rating_x,
                            MiscUtils.truncateFloat(rating.rating)),
                    unadjustedRating = application.getString(R.string.unadjusted_x_y,
                            MiscUtils.truncateFloat(rating.mu),
                            MiscUtils.truncateFloat(rating.sigma)))
        }

        return presentation
    }

}
