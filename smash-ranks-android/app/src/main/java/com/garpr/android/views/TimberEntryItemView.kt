package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.text.TextUtils
import android.util.AttributeSet
import android.util.SparseIntArray
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.misc.MiscUtils
import com.garpr.android.misc.Timber

class TimberEntryItemView : LinearLayout, BaseAdapterView<Timber.Entry> {

    lateinit private var mColors: SparseIntArray

    @BindView(R.id.tvStackTrace)
    lateinit protected var mStackTrace: TextView

    @BindView(R.id.tvTagAndMessage)
    lateinit protected var mTagAndMessage: TextView


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()
        ButterKnife.bind(this)
        mColors = SparseIntArray()
    }

    override fun setContent(content: Timber.Entry) {
        mTagAndMessage.text = resources.getString(R.string.tag_and_message, content.mTag,
                content.mMsg)

        if (TextUtils.isEmpty(content.mStackTrace)) {
            mStackTrace.text = ""
            mStackTrace.visibility = GONE
        } else {
            mStackTrace.text = content.mStackTrace
            mStackTrace.visibility = VISIBLE
        }

        var color = mColors.get(content.mColorAttrResId, -1)

        if (color == -1) {
            color = MiscUtils.getAttrColor(context, content.mColorAttrResId)
            mColors.put(content.mColorAttrResId, color)
        }

        mTagAndMessage.setTextColor(color)
        mStackTrace.setTextColor(color)
    }

}
