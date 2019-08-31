package com.garpr.android.features.setIdentity

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.features.common.adapters.BaseAdapterView
import kotlinx.android.synthetic.main.item_player_selection.view.*

class PlayerSelectionItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs), BaseAdapterView<Pair<AbsPlayer, Boolean>>, View.OnClickListener {

    private var _player: AbsPlayer? = null

    val player: AbsPlayer
        get() = requireNotNull(_player)

    var onClickListener: OnClickListener? = null

    interface OnClickListener {
        fun onClick(v: PlayerSelectionItemView)
    }

    init {
        setOnClickListener(this)
    }

    override fun onClick(v: View) {
        onClickListener?.onClick(this)
    }

    override fun setContent(content: Pair<AbsPlayer, Boolean>) {
        _player = content.first
        name.text = content.first.name
        radioButton.isChecked = content.second
    }

}
