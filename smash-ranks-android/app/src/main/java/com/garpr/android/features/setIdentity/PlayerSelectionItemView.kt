package com.garpr.android.features.setIdentity

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.clear
import com.garpr.android.features.base.BaseAdapterView
import kotlinx.android.synthetic.main.item_player_selection.view.*

class PlayerSelectionItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs), BaseAdapterView<AbsPlayer>, View.OnClickListener {

    interface Listeners {
        fun onClick(v: PlayerSelectionItemView)
        val selectedPlayer: AbsPlayer?
    }

    var player: AbsPlayer? = null
        set(value) {
            field = value

            if (value == null) {
                radioButton.isChecked = false
                name.clear()
            } else {
                radioButton.isChecked = (activity as? Listeners?)?.selectedPlayer == value
                name.text = value.name
            }
        }

    init {
        setOnClickListener(this)
    }

    override fun onClick(v: View) {
        (activity as? Listeners?)?.onClick(this)
    }

    override fun setContent(content: AbsPlayer) {
        player = content
    }

}
