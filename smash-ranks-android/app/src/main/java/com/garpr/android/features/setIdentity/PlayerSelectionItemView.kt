package com.garpr.android.features.setIdentity

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.garpr.android.data.models.AbsPlayer
import kotlinx.android.synthetic.main.item_player_selection.view.*

class PlayerSelectionItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), View.OnClickListener {

    private var _player: AbsPlayer? = null

    val player: AbsPlayer
        get() = checkNotNull(_player)

    var listener: Listener? = null

    init {
        setOnClickListener(this)
    }

    override fun onClick(v: View) {
        listener?.onClick(this)
    }

    fun setContent(player: AbsPlayer, isChecked: Boolean) {
        _player = player
        playerName.text = player.name
        radioButton.isChecked = isChecked
    }

    interface Listener {
        fun onClick(v: PlayerSelectionItemView)
    }

}
