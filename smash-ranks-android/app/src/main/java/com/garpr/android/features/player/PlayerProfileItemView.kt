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
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.extensions.verticalPositionInWindow
import com.garpr.android.misc.ColorListener
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.RegionHandleUtils
import kotlinx.android.synthetic.main.item_player_profile.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class PlayerProfileItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs), ColorListener, Heartbeat, KoinComponent, Refreshable {

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

    private var isFavorited: Boolean = false
    private var isIdentity: Boolean = false
    private var player: FullPlayer? = null

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
    protected val regionHandleUtils: RegionHandleUtils by inject()

    interface Listeners : ColorListener {
        fun onCompareClick(v: PlayerProfileItemView)
        fun onFavoriteOrUnfavoriteClick(v: PlayerProfileItemView)
        fun onShareClick(v: PlayerProfileItemView)
        fun onUrlClick(v: PlayerProfileItemView, url: String?)
    }

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

        listeners?.onPaletteBuilt(palette)

        repeat(childCount) {
            applyPaletteToView(palette, getChildAt(it))
        }
    }

    override fun refresh() {
        val player = this.player ?: return
        val region = regionHandleUtils.getRegion(context)
        val presentation = playerProfileManager.getPresentation(region, isFavorited, isIdentity,
                player, smashCompetitor)
        this.presentation = presentation

        if (presentation.avatar.isNullOrBlank()) {
            avatarContainer.visibility = View.GONE
        } else {
            avatar.setImageURI(presentation.avatar)
            avatarContainer.visibility = View.VISIBLE
        }

        playerTag.text = presentation.tag

        if (presentation.name.isNullOrBlank()) {
            name.visibility = View.GONE
        } else {
            name.text = presentation.name
            name.visibility = View.VISIBLE
        }

        if (presentation.mains.isNullOrBlank()) {
            mains.visibility = View.GONE
        } else {
            mains.text = presentation.mains
            mains.visibility = View.VISIBLE
        }

        if (presentation.aliases.isNullOrBlank()) {
            aliases.visibility = View.GONE
        } else {
            aliases.text = presentation.aliases
            aliases.visibility = View.VISIBLE
        }

        this.region.text = region.displayName

        if (presentation.rating.isNullOrBlank() || presentation.unadjustedRating.isNullOrBlank()) {
            rating.visibility = View.GONE
            unadjustedRating.visibility = View.GONE
        } else {
            rating.text = presentation.rating
            rating.visibility = View.VISIBLE

            unadjustedRating.text = presentation.unadjustedRating
            unadjustedRating.visibility = View.VISIBLE
        }

        if (presentation.twitch?.isNotBlank() == true
                || presentation.twitter?.isNotBlank() == true
                || presentation.youTube?.isNotBlank() == true) {
            if (presentation.twitch.isNullOrBlank()) {
                twitch.visibility = View.GONE
            } else {
                twitch.visibility = View.VISIBLE
            }

            if (presentation.twitter.isNullOrBlank()) {
                twitter.visibility = View.GONE
            } else {
                twitter.visibility = View.VISIBLE
            }

            if (presentation.youTube.isNullOrBlank()) {
                youTube.visibility = View.GONE
            } else {
                youTube.visibility = View.VISIBLE
            }

            websites.visibility = View.VISIBLE
            websitesDivider.visibility = View.VISIBLE
        } else {
            websites.visibility = View.GONE
            websitesDivider.visibility = View.GONE
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

    fun setContent(isFavorited: Boolean, isIdentity: Boolean, player: FullPlayer,
            smashCompetitor: SmashCompetitor?) {
        this.isFavorited = isFavorited
        this.isIdentity = isIdentity
        this.player = player
        this.smashCompetitor = smashCompetitor
        refresh()
    }

}
