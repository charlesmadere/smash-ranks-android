package com.garpr.android.features.player

import android.content.Context
import android.text.TextUtils
import com.garpr.android.R
import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.extensions.truncate
import com.garpr.android.features.player.PlayerProfileManager.Presentation
import com.garpr.android.misc.Constants
import com.garpr.android.repositories.IdentityRepository

class PlayerProfileManagerImpl(
        private val context: Context,
        private val identityRepository: IdentityRepository,
        private val smashRosterAvatarUrlHelper: SmashRosterAvatarUrlHelper
) : PlayerProfileManager {

    override fun getPresentation(region: AbsRegion, isFavorited: Boolean, player: FullPlayer,
            competitor: SmashCompetitor?): Presentation {
        var presentation = Presentation(
                isAddToFavoritesVisible = !isFavorited,
                isCompareVisible = identityRepository.hasIdentity && !identityRepository.isPlayer(player)
        )

        player.ratings?.get(region.id)?.let { rating ->
            presentation = presentation.copy(
                    rating = context.getString(R.string.rating_x, rating.rating.truncate()),
                    unadjustedRating = context.getString(
                            R.string.unadjusted_x_y,
                            rating.mu.truncate(),
                            rating.sigma.truncate()
                    )
            )
        }

        val uniqueAliases = player.uniqueAliases
        if (!uniqueAliases.isNullOrEmpty()) {
            presentation = presentation.copy(
                    aliases = context.resources.getQuantityString(
                            R.plurals.aliases_x,
                            uniqueAliases.size,
                            TextUtils.join(context.getText(R.string.delimiter), uniqueAliases)
                    )
            )
        }

        if (competitor == null) {
            presentation = presentation.copy(tag = player.name)
            return presentation
        }

        presentation = presentation.copy(
                name = competitor.name,
                tag = competitor.tag
        )

        val avatar = smashRosterAvatarUrlHelper.getAvatarUrl(
            avatarPath = competitor.avatar?.largeButFallbackToMediumThenOriginalThenSmall
        )

        if (!avatar.isNullOrBlank()) {
            presentation = presentation.copy(avatar = avatar)
        }

        val filteredMains = competitor.filteredMains
        if (!filteredMains.isNullOrEmpty()) {
            val mainsStrings = filteredMains.map { main ->
                context.getString(main.textResId)
            }

            presentation = presentation.copy(
                    mains = context.resources.getQuantityString(
                            R.plurals.mains_x,
                            filteredMains.size,
                            TextUtils.join(context.getText(R.string.delimiter), mainsStrings)
                    )
            )
        }

        val twitch = competitor.websites?.get(Constants.TWITCH)
        if (!twitch.isNullOrBlank()) {
            presentation = presentation.copy(twitch = twitch)
        }

        val twitter = competitor.websites?.get(Constants.TWITTER)
        if (!twitter.isNullOrBlank()) {
            presentation = presentation.copy(twitter = twitter)
        }

        val youTube = competitor.websites?.get(Constants.YOUTUBE)
        if (!youTube.isNullOrBlank()) {
            presentation = presentation.copy(youTube = youTube)
        }

        return presentation
    }

}
