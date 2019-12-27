package com.garpr.android.features.favoritePlayers

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.garpr.android.R
import com.garpr.android.data.models.FavoritePlayer
import kotlinx.android.synthetic.main.item_favorite_player.view.*

class FavoritePlayerItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), View.OnClickListener, View.OnLongClickListener {

    private val originalBackground: Drawable? = background
    private var _player: FavoritePlayer? = null

    val player: FavoritePlayer
        get() = checkNotNull(_player)

    @ColorInt
    private val cardBackgroundColor: Int = ContextCompat.getColor(context, R.color.card_background)

    var listeners: Listeners? = null

    interface Listeners {
        fun onClick(v: FavoritePlayerItemView)
        fun onLongClick(v: FavoritePlayerItemView)
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

    fun setContent(player: FavoritePlayer, isIdentity: Boolean) {
        _player = player
        name.text = player.name
        region.text = player.region.displayName

        if (isIdentity) {
            name.typeface = Typeface.DEFAULT_BOLD
            setBackgroundColor(cardBackgroundColor)
        } else {
            name.typeface = Typeface.DEFAULT
            ViewCompat.setBackground(this, originalBackground)
        }
    }

}
