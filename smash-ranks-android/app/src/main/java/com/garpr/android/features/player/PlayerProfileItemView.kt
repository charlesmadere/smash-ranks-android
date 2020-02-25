package com.garpr.android.features.player

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import androidx.palette.graphics.Palette
import com.garpr.android.R
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.extensions.verticalPositionInWindow
import com.garpr.android.misc.ColorListener
import com.garpr.android.misc.Heartbeat
import kotlinx.android.synthetic.main.item_player_profile.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class PlayerProfileItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs), ColorListener, Heartbeat, KoinComponent {

    private var _identity: AbsPlayer? = null

    val identity: AbsPlayer
        get() = checkNotNull(_identity)

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

    var colorListener: ColorListener? = null
    private var _player: FullPlayer? = null

    val player: FullPlayer
        get() = checkNotNull(_player)

    val ratingVerticalPositionInWindow: Int
        get() = rating.verticalPositionInWindow

    var listeners: Listeners? = null

    private val compareClickListener = OnClickListener {
        listeners?.onCompareClick(this)
    }

    private val favoriteOrUnfavoriteClickListener = OnClickListener {
        listeners?.onFavoriteOrUnfavoriteClick(this)
    }

    private val shareClickListener = OnClickListener {
        listeners?.onShareClick(this)
    }

    private val twitchClickListener = OnClickListener {
        listeners?.onUrlClick(this, presentation?.twitch)
    }

    private val twitterClickListener = OnClickListener {
        listeners?.onUrlClick(this, presentation?.twitter)
    }

    private val youTubeClickListener = OnClickListener {
        listeners?.onUrlClick(this, presentation?.youTube)
    }

    private var presentation: PlayerProfileManager.Presentation? = null
    private var smashCompetitor: SmashCompetitor? = null

    protected val playerProfileManager: PlayerProfileManager by inject()

    private fun applyPaletteToView(palette: Palette?, view: View?) {
        if (view is ColorListener) {
            view.onPaletteBuilt(palette)
        } else if (view is ViewGroup) {
            repeat(view.childCount) {
                applyPaletteToView(palette, view.getChildAt(it))
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        avatar.colorListener = this
        twitter.setOnClickListener(twitterClickListener)
        twitch.setOnClickListener(twitchClickListener)
        youTube.setOnClickListener(youTubeClickListener)
        favoriteOrUnfavorite.setOnClickListener(favoriteOrUnfavoriteClickListener)
        compare.setOnClickListener(compareClickListener)
        share.setOnClickListener(shareClickListener)
    }

    override fun onPaletteBuilt(palette: Palette?) {
        if (!isAlive) {
            return
        }

        colorListener?.onPaletteBuilt(palette)

        repeat(childCount) {
            applyPaletteToView(palette, getChildAt(it))
        }
    }

    fun setContent(
            identity: AbsPlayer?,
            region: AbsRegion,
            isFavorited: Boolean,
            player: FullPlayer,
            smashCompetitor: SmashCompetitor?
    ) {
        this._identity = identity
        this._player = player
        this.smashCompetitor = smashCompetitor

        val presentation = playerProfileManager.getPresentation(identity, region, isFavorited,
                player, smashCompetitor)
        this.presentation = presentation

        if (presentation.avatar.isNullOrBlank()) {
            avatarContainer.visibility = GONE
        } else {
            avatar.setImageURI(presentation.avatar)
            avatarContainer.visibility = VISIBLE
        }

        playerTag.text = presentation.tag

        if (presentation.name.isNullOrBlank()) {
            name.visibility = GONE
        } else {
            name.text = presentation.name
            name.visibility = VISIBLE
        }

        if (presentation.mains.isNullOrBlank()) {
            mains.visibility = GONE
        } else {
            mains.text = presentation.mains
            mains.visibility = VISIBLE
        }

        if (presentation.aliases.isNullOrBlank()) {
            aliases.visibility = GONE
        } else {
            aliases.text = presentation.aliases
            aliases.visibility = VISIBLE
        }

        regionDisplayName.text = region.displayName

        if (presentation.rating.isNullOrBlank() || presentation.unadjustedRating.isNullOrBlank()) {
            rating.visibility = GONE
            unadjustedRating.visibility = GONE
        } else {
            rating.text = presentation.rating
            rating.visibility = VISIBLE

            unadjustedRating.text = presentation.unadjustedRating
            unadjustedRating.visibility = VISIBLE
        }

        if (presentation.twitch.isNullOrBlank() && presentation.twitter.isNullOrBlank() &&
                presentation.youTube.isNullOrBlank()) {
            websites.visibility = GONE
            websitesDivider.visibility = GONE
        } else {
            if (presentation.twitch.isNullOrBlank()) {
                twitch.visibility = GONE
            } else {
                twitch.visibility = VISIBLE
            }

            if (presentation.twitter.isNullOrBlank()) {
                twitter.visibility = GONE
            } else {
                twitter.visibility = VISIBLE
            }

            if (presentation.youTube.isNullOrBlank()) {
                youTube.visibility = GONE
            } else {
                youTube.visibility = VISIBLE
            }

            websites.visibility = VISIBLE
            websitesDivider.visibility = VISIBLE
        }

        if (presentation.isAddToFavoritesVisible) {
            favoriteOrUnfavorite.setText(R.string.favorite)
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(favoriteOrUnfavorite,
                    0, R.drawable.ic_favorite_white_24dp, 0, 0)
        } else {
            favoriteOrUnfavorite.setText(R.string.unfavorite)
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(favoriteOrUnfavorite,
                    0, R.drawable.ic_favorite_border_white_24dp, 0, 0)
        }

        favoriteOrUnfavorite.refresh()

        if (presentation.isCompareVisible) {
            compare.visibility = View.VISIBLE
        } else {
            compare.visibility = View.GONE
        }
    }

    interface Listeners {
        fun onCompareClick(v: PlayerProfileItemView)
        fun onFavoriteOrUnfavoriteClick(v: PlayerProfileItemView)
        fun onShareClick(v: PlayerProfileItemView)
        fun onUrlClick(v: PlayerProfileItemView, url: String?)
    }

}
