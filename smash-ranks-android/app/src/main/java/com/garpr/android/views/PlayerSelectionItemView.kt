package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.optActivity
import com.garpr.android.models.AbsPlayer
import kotterknife.bindView

class PlayerSelectionItemView : LinearLayout, BaseAdapterView<AbsPlayer>, View.OnClickListener {

    private val radioButton: RadioButton by bindView(R.id.radioButton)
    private val name: TextView by bindView(R.id.tvName)


    interface Listeners {
        fun onClick(v: PlayerSelectionItemView)
        val selectedPlayer: AbsPlayer?
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    var player: AbsPlayer? = null
        set(value) {
            field = value

            if (value == null) {
                radioButton.isChecked = false
                name.clear()
            } else {
                radioButton.isChecked = (context.optActivity() as? Listeners)?.selectedPlayer == value
                name.text = value.name
            }
        }

    override fun onClick(v: View) {
        (context.optActivity() as? Listeners)?.onClick(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setOnClickListener(this)
    }

    override fun setContent(content: AbsPlayer) {
        player = content
    }

}
