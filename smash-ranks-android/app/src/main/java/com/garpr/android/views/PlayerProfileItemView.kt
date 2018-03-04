package com.garpr.android.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v4.widget.TextViewCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.activities.HeadToHeadActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.getActivity
import com.garpr.android.misc.*
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

    private val addToOrRemoveFromFavorites: TintedTextView by bindView(R.id.ttvAddToOrRemoveFromFavorites)
    private val aliases: TextView by bindView(R.id.tvAliases)
    private val rating: TextView by bindView(R.id.tvRating)
    private val setAsYourIdentity: TextView by bindView(R.id.tvSetAsYourIdentity)
    private val share: TextView by bindView(R.id.tvShare)
    private val unadjustedRating: TextView by bindView(R.id.tvUnadjustedRating)
    private val viewYourselfVsThisOpponent: TextView by bindView(R.id.tvViewYourselfVsThisOpponent)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun animateAwaySetAsYourIdentity() {
        val duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()

        setAsYourIdentity.animate()
                .alpha(0f)
                .setDuration(duration)
                .setInterpolator(AccelerateInterpolator())
                .withEndAction {
                    val animator = ValueAnimator.ofInt(setAsYourIdentity.height, 0)
                    animator.duration = duration
                    animator.interpolator = DecelerateInterpolator()

                    val layoutParams = setAsYourIdentity.layoutParams
                    animator.addUpdateListener {
                        layoutParams.height = it.animatedValue as Int
                        setAsYourIdentity.layoutParams = layoutParams
                    }

                    animator.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            setAsYourIdentity.visibility = View.GONE
                        }
                    })

                    animator.start()
                }
                .start()
    }

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

    override fun onFavoritePlayersChanged(favoritePlayersManager: FavoritePlayersManager) {
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

        setAsYourIdentity.setOnClickListener {
            if (identityManager.hasIdentity) {
                // prevent double clicks
                return@setOnClickListener
            }

            val player = fullPlayer ?: throw RuntimeException("fullPlayer is null")
            identityManager.setIdentity(player, regionManager.getRegion(context))
        }

        addToOrRemoveFromFavorites.setOnClickListener {
            fullPlayer?.let {
                if (favoritePlayersManager.contains(it)) {
                    favoritePlayersManager.removePlayer(it)
                } else {
                    favoritePlayersManager.addPlayer(it, regionManager.getRegion(context))
                }
            }
        }

        viewYourselfVsThisOpponent.setOnClickListener {
            val identity = identityManager.identity ?: throw RuntimeException("identity is null")
            val player = fullPlayer ?: throw RuntimeException("fullPlayer is null")
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
        val uniqueAliases = player.uniqueAliases

        if (uniqueAliases == null || uniqueAliases.isEmpty()) {
            aliases.visibility = View.GONE
        } else {
            aliases.text = context.getString(R.string.aliases_x,
                    TextUtils.join(context.getText(R.string.delimiter), uniqueAliases))
            aliases.visibility = View.VISIBLE
        }

        val scores = player.ratings?.get(regionManager.getRegion(context).id)
        if (scores == null) {
            rating.visibility = View.GONE
            unadjustedRating.visibility = View.GONE
        } else {
            rating.text = context.getString(R.string.rating_x,
                    MiscUtils.truncateFloat(scores.rating))
            rating.visibility = View.VISIBLE

            unadjustedRating.text = context.getString(R.string.unadjusted_x_y,
                    MiscUtils.truncateFloat(scores.mu), MiscUtils.truncateFloat(scores.sigma))
            unadjustedRating.visibility = View.VISIBLE
        }

        if (favoritePlayersManager.contains(player)) {
            addToOrRemoveFromFavorites.setText(R.string.remove_from_favorites)
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(addToOrRemoveFromFavorites,
                    0, 0, R.drawable.ic_favorite_border_white_24dp, 0)
        } else {
            addToOrRemoveFromFavorites.setText(R.string.add_to_favorites)
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(addToOrRemoveFromFavorites,
                    0, 0, R.drawable.ic_favorite_white_24dp, 0)
        }

        addToOrRemoveFromFavorites.refresh()

        if (identityManager.hasIdentity) {
            if (setAsYourIdentity.visibility == View.VISIBLE) {
                animateAwaySetAsYourIdentity()
            } else {
                setAsYourIdentity.visibility = View.GONE
            }

            if (identityManager.isPlayer(player)) {
                viewYourselfVsThisOpponent.visibility = View.GONE
            } else {
                viewYourselfVsThisOpponent.visibility = View.VISIBLE
            }
        } else {
            setAsYourIdentity.visibility = View.VISIBLE
            viewYourselfVsThisOpponent.visibility = View.GONE
        }
    }

    override fun setContent(content: FullPlayer) {
        fullPlayer = content
    }

}
