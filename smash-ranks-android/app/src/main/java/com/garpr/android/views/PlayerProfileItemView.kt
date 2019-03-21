package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import androidx.palette.graphics.Palette
import com.garpr.android.R
import com.garpr.android.activities.HeadToHeadActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.requireActivity
import com.garpr.android.extensions.verticalPositionInWindow
import com.garpr.android.managers.FavoritePlayersManager
import com.garpr.android.managers.IdentityManager
import com.garpr.android.managers.PlayerProfileManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.ColorListener
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.ShareUtils
import com.garpr.android.sync.roster.SmashRosterSyncManager
import kotlinx.android.synthetic.main.item_player_profile.view.*
import javax.inject.Inject

class PlayerProfileItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleLinearLayout(context, attrs), BaseAdapterView<FullPlayer>, ColorListener,
        FavoritePlayersManager.OnFavoritePlayersChangeListener,
        IdentityManager.OnIdentityChangeListener, Refreshable,
        SmashRosterSyncManager.OnSyncListeners {

    private var presentation: PlayerProfileManager.Presentation? = null

    @Inject
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var playerProfileManager: PlayerProfileManager

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var shareUtils: ShareUtils

    @Inject
    protected lateinit var smashRosterSyncManager: SmashRosterSyncManager


    private var fullPlayer: FullPlayer? = null
        set(value) {
            field = value
            refresh()
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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        favoritePlayersManager.addListener(this)
        identityManager.addListener(this)
        smashRosterSyncManager.addListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        favoritePlayersManager.removeListener(this)
        identityManager.removeListener(this)
        smashRosterSyncManager.removeListener(this)
    }

    override fun onFavoritePlayersChange(favoritePlayersManager: FavoritePlayersManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        appComponent.inject(this)
        favoritePlayersManager.addListener(this)
        identityManager.addListener(this)

        avatar.colorListener = this

        twitter.setOnClickListener {
            presentation?.let { p -> shareUtils.openUrl(context, p.twitter) }
        }

        twitch.setOnClickListener {
            presentation?.let { p -> shareUtils.openUrl(context, p.twitch) }
        }

        youTube.setOnClickListener {
            presentation?.let { p -> shareUtils.openUrl(context, p.youTube) }
        }

        otherWebsite.setOnClickListener {
            presentation?.let { p -> shareUtils.openUrl(context, p.otherWebsite) }
        }

        favoriteOrUnfavorite.setOnClickListener {
            fullPlayer?.let { p ->
                if (p in favoritePlayersManager) {
                    favoritePlayersManager.removePlayer(p)
                } else {
                    favoritePlayersManager.addPlayer(p, regionManager.getRegion(context))
                }
            }
        }

        viewYourselfVsThisOpponent.setOnClickListener {
            val identity = identityManager.identity ?: throw NullPointerException("identity is null")
            val player = fullPlayer ?: throw NullPointerException("fullPlayer is null")
            context.startActivity(HeadToHeadActivity.getLaunchIntent(context, identity, player,
                    regionManager.getRegion(context)))
        }

        share.setOnClickListener {
            fullPlayer?.let { p -> shareUtils.sharePlayer(requireActivity(), p) }
        }
    }

    override fun onIdentityChange(identityManager: IdentityManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun onPaletteBuilt(palette: Palette?) {
        (activity as? ColorListener?)?.onPaletteBuilt(palette)

        repeat(childCount) {
            applyPaletteToView(palette, getChildAt(it))
        }
    }

    override fun onSmashRosterSyncBegin(smashRosterSyncManager: SmashRosterSyncManager) {
        // intentionally empty
    }

    override fun onSmashRosterSyncComplete(smashRosterSyncManager: SmashRosterSyncManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        val player = fullPlayer ?: return
        val region = regionManager.getRegion(context)
        val presentation = playerProfileManager.getPresentation(player, region)
        this.presentation = presentation

        if (presentation.avatar.isNullOrBlank()) {
            avatar.visibility = View.GONE
        } else {
            avatar.setImageURI(presentation.avatar)
            avatar.visibility = View.VISIBLE
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
                || presentation.youTube?.isNotBlank() == true
                || presentation.otherWebsite?.isNotBlank() == true) {
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

            if (presentation.otherWebsite.isNullOrBlank()) {
                otherWebsite.visibility = View.GONE
            } else {
                otherWebsite.visibility = View.VISIBLE
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

        if (presentation.isViewYourselfVsThisOpponentVisible) {
            viewYourselfVsThisOpponent.visibility = View.VISIBLE
        } else {
            viewYourselfVsThisOpponent.visibility = View.GONE
        }
    }

    val ratingVerticalPositionInWindow: Int
        get() = rating.verticalPositionInWindow

    override fun setContent(content: FullPlayer) {
        fullPlayer = content
    }

}
