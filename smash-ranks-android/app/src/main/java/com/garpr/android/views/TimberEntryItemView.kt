package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.util.SparseIntArray
import android.widget.LinearLayout
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.misc.Timber
import kotterknife.bindView

class TimberEntryItemView : LinearLayout, BaseAdapterView<Timber.Entry> {

    private val mColors = SparseIntArray()
    private val mStackTrace: TextView by bindView(R.id.tvStackTrace)
    private val mTagAndMessage: TextView by bindView(R.id.tvTagAndMessage)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mColors.clear()
    }

    override fun setContent(content: Timber.Entry) {
        mTagAndMessage.text = resources.getString(R.string.tag_and_message, content.mTag,
                content.mMsg)

        if (content.mStackTrace.isNullOrEmpty()) {
            mStackTrace.text = ""
            mStackTrace.visibility = GONE
        } else {
            mStackTrace.text = content.mStackTrace
            mStackTrace.visibility = VISIBLE
        }

        var color = mColors.get(content.mColorAttrResId, -1)

        if (color == -1) {
            color = context.getAttrColor(content.mColorAttrResId)
            mColors.put(content.mColorAttrResId, color)
        }

        mTagAndMessage.setTextColor(color)
        mStackTrace.setTextColor(color)
    }

}
