package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.optActivity
import com.garpr.android.models.Region
import kotterknife.bindView

class RegionSelectionItemView : LinearLayout, BaseAdapterView<Region>, View.OnClickListener {

    var mContent: Region? = null
        private set

    private val mRadioButton: RadioButton by bindView(R.id.radioButton)
    private val mId: TextView by bindView(R.id.tvId)
    private val mDisplayName: TextView by bindView(R.id.tvDisplayName)


    interface Listeners {
        fun onClick(v: RegionSelectionItemView)
        val selectedRegion: Region?
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
            attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes)

    override fun onClick(v: View) {
        val activity = context.optActivity()

        if (activity is Listeners) {
            (activity as Listeners).onClick(this)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setOnClickListener(this)
    }

    override fun setContent(content: Region) {
        mContent = content

        mDisplayName.text = content.displayName
        mId.text = content.id

        val activity = context.optActivity()

        if (activity is Listeners) {
            mRadioButton.isChecked = content == (activity as Listeners).selectedRegion
        } else {
            mRadioButton.isChecked = false
        }
    }

}
