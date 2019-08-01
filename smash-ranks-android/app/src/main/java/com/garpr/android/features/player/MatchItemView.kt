package com.garpr.android.features.player

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.garpr.android.R
import com.garpr.android.data.models.Match
import com.garpr.android.data.models.MatchResult
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.fragmentManager
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.features.common.views.LifecycleFrameLayout
import com.garpr.android.misc.Refreshable
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.item_match.view.*
import javax.inject.Inject

class MatchItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleFrameLayout(context, attrs), IdentityRepository.OnIdentityChangeListener, Refreshable,
        View.OnClickListener, View.OnLongClickListener {

    private val originalBackground: Drawable? = background

    var match: Match? = null
        set(value) {
            field = value
            refresh()
        }

    @Inject
    protected lateinit var favoritePlayersRepository: FavoritePlayersRepository

    @Inject
    protected lateinit var identityRepository: IdentityRepository

    @Inject
    protected lateinit var regionRepository: RegionRepository


    interface OnClickListener {
        fun onClick(v: MatchItemView)
    }

    init {
        setOnClickListener(this)
        setOnLongClickListener(this)

        if (!isInEditMode) {
            appComponent.inject(this)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        identityRepository.addListener(this)
        refresh()
    }

    override fun onClick(v: View) {
        val match = this.match ?: return
        val activity = this.activity

        if (activity is OnClickListener) {
            activity.onClick(this)
        } else {
            val opponent = match.opponent
            context.startActivity(PlayerActivity.getLaunchIntent(context, opponent.id,
                    regionRepository.getRegion(context)))
        }
    }

    override fun onDetachedFromWindow() {
        identityRepository.removeListener(this)
        super.onDetachedFromWindow()
    }

    override fun onIdentityChange(identityRepository: IdentityRepository) {
        if (isAlive) {
            refresh()
        }
    }

    override fun onLongClick(v: View): Boolean {
        return favoritePlayersRepository.showAddOrRemovePlayerDialog(fragmentManager,
                match?.opponent, regionRepository.getRegion(context))
    }

    override fun refresh() {
        val match = this.match

        if (identityRepository.isPlayer(match?.opponent)) {
            name.typeface = Typeface.DEFAULT_BOLD
            setBackgroundColor(ContextCompat.getColor(context, R.color.card_background))
        } else {
            name.typeface = Typeface.DEFAULT
            ViewCompat.setBackground(this, originalBackground)
        }

        if (match == null) {
            name.clear()
            return
        }

        name.text = match.opponent.name

        name.setTextColor(when (match.result) {
            MatchResult.EXCLUDED -> context.getAttrColor(android.R.attr.textColorSecondary)
            MatchResult.LOSE -> ContextCompat.getColor(context, R.color.lose)
            MatchResult.WIN -> ContextCompat.getColor(context, R.color.win)
        })
    }

}
