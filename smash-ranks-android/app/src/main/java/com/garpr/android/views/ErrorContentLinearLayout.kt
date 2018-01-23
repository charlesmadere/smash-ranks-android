package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import com.garpr.android.R
import com.garpr.android.misc.Constants

class ErrorContentLinearLayout : NoContentLinearLayout {

    private var originalLine2Text: CharSequence? = null


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()

        originalLine2Text = line2.text
    }

    fun setVisibility(visibility: Int, errorCode: Int) {
        super.setVisibility(visibility)

        if (errorCode == Constants.ERROR_CODE_BAD_REQUEST) {
            line2.setText(R.string.region_mismatch_error)
        } else {
            line2.text = originalLine2Text
        }
    }

}
