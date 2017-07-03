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
import com.garpr.android.misc.MiscUtils
import com.garpr.android.models.AbsPlayer
import kotterknife.bindView

class PlayerSelectionItemView : LinearLayout, BaseAdapterView<AbsPlayer>, View.OnClickListener {

    var mContent: AbsPlayer? = null
        private set

    private val mRadioButton: RadioButton by bindView(R.id.radioButton)
    private val mName: TextView by bindView(R.id.tvName)


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

    override fun onClick(v: View) {
        val activity = MiscUtils.optActivity(context)

        if (activity is Listeners) {
            activity.onClick(this)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setOnClickListener(this)
    }

    override fun setContent(content: AbsPlayer) {
        mContent = content
        mName.text = content.name

        val activity = MiscUtils.optActivity(context)

        if (activity is Listeners) {
            mRadioButton.isChecked = mContent == activity.selectedPlayer
        } else {
            mRadioButton.isChecked = false
        }
    }

}
