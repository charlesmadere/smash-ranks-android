package com.garpr.android.features.players

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.garpr.android.R
import com.garpr.android.data.models.AbsPlayer
import kotlinx.android.synthetic.main.item_player.view.*

class PlayerItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : FrameLayout(context, attrs), View.OnClickListener, View.OnLongClickListener {

    private var _player: AbsPlayer? = null

    val player: AbsPlayer
        get() = checkNotNull(_player)

    private val originalBackground: Drawable? = background

    @ColorInt
    private val cardBackgroundColor: Int = ContextCompat.getColor(context, R.color.card_background)

    var listeners: Listeners? = null

    interface Listeners {
        fun onClick(v: PlayerItemView)
        fun onLongClick(v: PlayerItemView)
    }

    init {
        setOnClickListener(this)
        setOnLongClickListener(this)
    }

    override fun onClick(v: View) {
        listeners?.onClick(this)
    }

    override fun onLongClick(v: View): Boolean {
        listeners?.onLongClick(this)
        return true
    }

    fun setContent(player: AbsPlayer, isIdentity: Boolean) {
        _player = player
        name.text = player.name

        if (isIdentity) {
            name.typeface = Typeface.DEFAULT_BOLD
            setBackgroundColor(cardBackgroundColor)
        } else {
            name.typeface = Typeface.DEFAULT
            ViewCompat.setBackground(this, originalBackground)
        }
    }

}
