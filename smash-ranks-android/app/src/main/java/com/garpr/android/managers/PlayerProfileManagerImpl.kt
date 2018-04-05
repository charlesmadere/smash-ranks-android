package com.garpr.android.managers

import android.app.Application
import android.text.TextUtils
import com.garpr.android.R
import com.garpr.android.managers.PlayerProfileManager.Presentation
import com.garpr.android.misc.Constants
import com.garpr.android.misc.MiscUtils
import com.garpr.android.misc.SmashRosterStorage
import com.garpr.android.models.AbsRegion
import com.garpr.android.models.FullPlayer
import com.garpr.android.models.Region

class PlayerProfileManagerImpl(
        val application: Application,
        val favoritePlayersManager: FavoritePlayersManager,
        val identityManager: IdentityManager,
        val regionManager: RegionManager,
        val smashRosterStorage: SmashRosterStorage
) : PlayerProfileManager {

    override fun getPresentation(player: FullPlayer, region: AbsRegion): Presentation {
        var presentation = Presentation(
                isAddToFavoritesVisible = !favoritePlayersManager.contains(player),
                isViewYourselfVsThisOpponentVisible = identityManager.hasIdentity &&
                        !identityManager.isPlayer(player))

        val uniqueAliases = player.uniqueAliases
        if (uniqueAliases?.isNotEmpty() == true) {
            presentation = presentation.copy(aliases = application.resources.getQuantityString(
                    R.plurals.aliases_x, uniqueAliases.size, TextUtils.join(
                    application.getText(R.string.delimiter), uniqueAliases)))
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

        val competitor = if (region is Region) {
            smashRosterStorage.getSmashCompetitor(region, player.id)
        } else {
            null
        }

        @Suppress("FoldInitializerAndIfToElvis")
        if (competitor == null) {
            return presentation
        }

        presentation = presentation.copy(name = competitor.name)

        val avatar = competitor.avatar?.mediumButFallbackToLargeThenSmall

        if (avatar?.isNotBlank() == true) {
            presentation = presentation.copy(avatar = avatar)
        }

        val filteredMains = competitor.filteredMains
        if (filteredMains?.isNotEmpty() == true) {
            val mainsStrings = mutableListOf<String>()

            for (main in filteredMains) {
                mainsStrings.add(application.getString(main.textResId))
            }

            presentation = presentation.copy(mains = application.resources.getQuantityString(
                    R.plurals.mains_x, filteredMains.size, TextUtils.join(
                    application.getText(R.string.delimiter), mainsStrings)))
        }

        val twitch = competitor.websites?.get(Constants.TWITCH)
        if (twitch?.isNotBlank() == true) {
            presentation = presentation.copy(twitch = twitch)
        }

        val twitter = competitor.websites?.get(Constants.TWITTER)
        if (twitter?.isNotBlank() == true) {
            presentation = presentation.copy(twitter = twitter)
        }

        return presentation
    }

}
