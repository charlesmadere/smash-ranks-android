package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v4.widget.TextViewCompat
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.activities.HeadToHeadActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.getActivity
import com.garpr.android.extensions.verticalPositionInWindow
import com.garpr.android.managers.FavoritePlayersManager
import com.garpr.android.managers.IdentityManager
import com.garpr.android.managers.PlayerProfileManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.ShareUtils
import com.garpr.android.models.FullPlayer
import kotterknife.bindView
import javax.inject.Inject

class PlayerProfileItemView : LifecycleLinearLayout, BaseAdapterView<FullPlayer>,
        FavoritePlayersManager.OnFavoritePlayersChangeListener,
        IdentityManager.OnIdentityChangeListener, Refreshable {

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

    private val addToOrRemoveFromFavorites: TintedTextView by bindView(R.id.tvAddToOrRemoveFromFavorites)
    private val aliases: TextView by bindView(R.id.tvAliases)
    private val name: TextView by bindView(R.id.tvName)
    private val rating: TextView by bindView(R.id.tvRating)
    private val region: TextView by bindView(R.id.tvRegion)
    private val share: TextView by bindView(R.id.tvShare)
    private val unadjustedRating: TextView by bindView(R.id.tvUnadjustedRating)
    private val viewYourselfVsThisOpponent: TextView by bindView(R.id.tvViewYourselfVsThisOpponent)


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
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        favoritePlayersManager.removeListener(this)
        identityManager.removeListener(this)
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

        App.get().appComponent.inject(this)
        favoritePlayersManager.addListener(this)
        identityManager.addListener(this)

        addToOrRemoveFromFavorites.setOnClickListener {
            val player = fullPlayer ?: return@setOnClickListener

            if (favoritePlayersManager.contains(player)) {
                favoritePlayersManager.removePlayer(player)
            } else {
                favoritePlayersManager.addPlayer(player, regionManager.getRegion(context))
            }
        }

        viewYourselfVsThisOpponent.setOnClickListener {
            val identity = identityManager.identity ?: throw NullPointerException("identity is null")
            val player = fullPlayer ?: throw NullPointerException("fullPlayer is null")
            context.startActivity(HeadToHeadActivity.getLaunchIntent(context, identity, player,
                    regionManager.getRegion(context)))
        }

        share.setOnClickListener {
            fullPlayer?.let { shareUtils.sharePlayer(context.getActivity(), it) }
        }
    }

    override fun onIdentityChange(identityManager: IdentityManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        val player = fullPlayer ?: return
        name.text = player.name

        val region = regionManager.getRegion(context)
        val presentation = playerProfileManager.getPresentation(player, region)

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

        if (presentation.isAddToFavoritesVisible) {
            addToOrRemoveFromFavorites.setText(R.string.add_to_favorites)
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(addToOrRemoveFromFavorites,
                    0, R.drawable.ic_favorite_white_24dp, 0, 0)
        } else {
            addToOrRemoveFromFavorites.setText(R.string.remove_from_favorites)
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(addToOrRemoveFromFavorites,
                    0, R.drawable.ic_favorite_border_white_24dp, 0, 0)
        }

        addToOrRemoveFromFavorites.refresh()

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
