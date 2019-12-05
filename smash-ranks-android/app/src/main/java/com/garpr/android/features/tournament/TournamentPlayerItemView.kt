package com.garpr.android.features.tournament

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.garpr.android.R
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.extensions.clear
import com.garpr.android.features.common.adapters.BaseAdapterView
import com.garpr.android.features.common.views.LifecycleFrameLayout
import com.garpr.android.misc.Refreshable
import com.garpr.android.repositories.IdentityRepository
import kotlinx.android.synthetic.main.item_tournament_player.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class TournamentPlayerItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleFrameLayout(context, attrs), BaseAdapterView<AbsPlayer>,
        IdentityRepository.OnIdentityChangeListener, KoinComponent, Refreshable,
        View.OnClickListener, View.OnLongClickListener {

    private var _player: AbsPlayer? = null

    val player: AbsPlayer
        get() = checkNotNull(_player)

    private val originalBackground: Drawable? = background

    @ColorInt
    private val cardBackgroundColor: Int = ContextCompat.getColor(context, R.color.card_background)

    var listeners: Listeners? = null

    protected val identityRepository: IdentityRepository by inject()

    interface Listeners {
        fun onClick(v: TournamentPlayerItemView)
        fun onLongClick(v: TournamentPlayerItemView)
    }

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
        listeners?.onClick(this)
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
        listeners?.onLongClick(this)
        return true
    }

    override fun refresh() {
        val player = _player

        if (player == null) {
            name.clear()
            return
        }

        if (identityRepository.isPlayer(player)) {
            name.typeface = Typeface.DEFAULT_BOLD
            setBackgroundColor(cardBackgroundColor)
        } else {
            name.typeface = Typeface.DEFAULT
            ViewCompat.setBackground(this, originalBackground)
        }

        name.text = player.name
    }

    override fun setContent(content: AbsPlayer) {
        _player = content
        refresh()
    }

}
