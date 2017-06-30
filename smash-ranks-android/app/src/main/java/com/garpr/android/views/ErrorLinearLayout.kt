package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.misc.Constants
import kotterknife.bindView

class ErrorLinearLayout : LinearLayout {

    private var mOriginalLine2: CharSequence? = null

    private val mLine1: TextView by bindView(R.id.recyclerViewLine1)
    private val mLine2: TextView by bindView(R.id.recyclerViewLine2)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()
        mOriginalLine2 = mLine2.text
    }

    fun setVisibility(visibility: Int, errorCode: Int) {
        super.setVisibility(visibility)

        if (errorCode == Constants.ERROR_CODE_BAD_REQUEST) {
            mLine2.setText(R.string.region_mismatch_error)
        } else {
            mLine2.text = mOriginalLine2
        }
    }

}
