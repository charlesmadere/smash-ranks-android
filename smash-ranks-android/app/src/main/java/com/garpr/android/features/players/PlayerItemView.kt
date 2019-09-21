package com.garpr.android.features.players

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.garpr.android.R
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.fragmentManager
import com.garpr.android.features.common.views.LifecycleFrameLayout
import com.garpr.android.features.player.PlayerActivity
import com.garpr.android.misc.Refreshable
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.item_player.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class PlayerItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleFrameLayout(context, attrs), KoinComponent,
        IdentityRepository.OnIdentityChangeListener, Refreshable, View.OnClickListener,
        View.OnLongClickListener {

    var player: AbsPlayer? = null
        set(value) {
            field = value
            refresh()
        }

    private val originalBackground: Drawable? = background

    protected val favoritePlayersRepository: FavoritePlayersRepository by inject()
    protected val identityRepository: IdentityRepository by inject()
    protected val regionRepository: RegionRepository by inject()

    init {
        setOnClickListener(this)
        setOnLongClickListener(this)
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
        val player = this.player ?: return
        context.startActivity(PlayerActivity.getLaunchIntent(context, player,
                regionRepository.getRegion(context)))
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
        return favoritePlayersRepository.showAddOrRemovePlayerDialog(fragmentManager, player,
                regionRepository.getRegion(context))
    }

    override fun refresh() {
        val player = this.player

        if (identityRepository.isPlayer(player)) {
            name.typeface = Typeface.DEFAULT_BOLD
            setBackgroundColor(ContextCompat.getColor(context, R.color.card_background))
        } else {
            name.typeface = Typeface.DEFAULT
            ViewCompat.setBackground(this, originalBackground)
        }

        if (player == null) {
            name.clear()
        } else {
            name.text = player.name
        }
    }

}
