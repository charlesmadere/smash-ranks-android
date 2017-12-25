package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.optActivity
import com.garpr.android.models.Region
import kotterknife.bindView

class RegionSelectionItemView : FrameLayout, BaseAdapterView<Region>, View.OnClickListener {

    private val radioButton: RadioButton by bindView(R.id.radioButton)
    private val displayName: TextView by bindView(R.id.tvDisplayName)
    private val id: TextView by bindView(R.id.tvId)


    interface Listeners {
        fun onClick(v: RegionSelectionItemView)
        val selectedRegion: Region?
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun clear() {
        radioButton.isChecked = false
        displayName.clear()
        id.clear()
    }

    override fun onClick(v: View) {
        (context.optActivity() as? Listeners)?.onClick(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setOnClickListener(this)
    }

    var region: Region? = null
        private set(value) {
            field = value

            if (value == null) {
                clear()
                return
            }

            radioButton.isChecked = value == (context.optActivity() as? Listeners)?.selectedRegion
            displayName.text = value.displayName
            id.text = value.id
        }

    override fun setContent(content: Region) {
        region = content
    }

}
