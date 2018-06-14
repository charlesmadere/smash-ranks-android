package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v4.content.ContextCompat
import android.support.v4.widget.TextViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.garpr.android.R
import com.garpr.android.activities.HeadToHeadActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.requireActivity
import com.garpr.android.extensions.verticalPositionInWindow
import com.garpr.android.managers.FavoritePlayersManager
import com.garpr.android.managers.IdentityManager
import com.garpr.android.managers.PlayerProfileManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.MiscUtils
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.ShareUtils
import com.garpr.android.models.FullPlayer
import com.garpr.android.sync.SmashRosterSyncManager
import kotterknife.bindView
import javax.inject.Inject

class PlayerProfileItemView : LifecycleLinearLayout, BaseAdapterView<FullPlayer>,
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

    private val avatar: SimpleDraweeView by bindView(R.id.sdvAvatar)
    private val aliases: TextView by bindView(R.id.tvAliases)
    private val favoriteOrUnfavorite: TintedTextView by bindView(R.id.tvFavoriteOrUnfavorite)
    private val mains: TextView by bindView(R.id.tvMains)
    private val name: TextView by bindView(R.id.tvName)
    private val rating: TextView by bindView(R.id.tvRating)
    private val region: TextView by bindView(R.id.tvRegion)
    private val share: TextView by bindView(R.id.tvShare)
    private val tag: TextView by bindView(R.id.tvTag)
    private val otherWebsite: TextView by bindView(R.id.tvOtherWebsite)
    private val twitch: TextView by bindView(R.id.tvTwitch)
    private val twitter: TextView by bindView(R.id.tvTwitter)
    private val youTube: TextView by bindView(R.id.tvYouTube)
    private val unadjustedRating: TextView by bindView(R.id.tvUnadjustedRating)
    private val viewYourselfVsThisOpponent: TextView by bindView(R.id.tvViewYourselfVsThisOpponent)
    private val websites: ViewGroup by bindView(R.id.vgWebsites)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var fullPlayer: FullPlayer? = null
        set(value) {
            field = value
            refresh()
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

        avatar.hierarchy.apply {
            val placeholderImage = MiscUtils.tintDrawable(ContextCompat.getDrawable(context,
                    R.drawable.controller_placeholder),
                    context.getAttrColor(android.R.attr.textColorSecondary))
            setPlaceholderImage(placeholderImage)
            setFailureImage(placeholderImage)
        }

        twitter.setOnClickListener {
            presentation?.let { shareUtils.openUrl(context, it.twitter) }
        }

        twitch.setOnClickListener {
            presentation?.let { shareUtils.openUrl(context, it.twitch) }
        }

        youTube.setOnClickListener {
            presentation?.let { shareUtils.openUrl(context, it.youTube) }
        }

        otherWebsite.setOnClickListener {
            presentation?.let { shareUtils.openUrl(context, it.otherWebsite) }
        }

        favoriteOrUnfavorite.setOnClickListener {
            fullPlayer?.let {
                if (it in favoritePlayersManager) {
                    favoritePlayersManager.removePlayer(it)
                } else {
                    favoritePlayersManager.addPlayer(it, regionManager.getRegion(context))
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
            fullPlayer?.let { shareUtils.sharePlayer(requireActivity(), it) }
        }
    }

    override fun onIdentityChange(identityManager: IdentityManager) {
        if (isAlive) {
            refresh()
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

        tag.text = presentation.tag

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
        } else {
            websites.visibility = View.GONE
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

    val regionVerticalPositionInWindow: Int
        get() = region.verticalPositionInWindow

    override fun setContent(content: FullPlayer) {
        fullPlayer = content
    }

}
