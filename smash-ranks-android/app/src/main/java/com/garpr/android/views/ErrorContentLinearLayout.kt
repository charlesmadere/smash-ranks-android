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

    private var originalDescriptionText: CharSequence? = null


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()

        originalDescriptionText = descriptionText
    }

    fun setVisibility(visibility: Int, errorCode: Int) {
        super.setVisibility(visibility)

        descriptionText = if (errorCode == Constants.ERROR_CODE_BAD_REQUEST) {
            context.getText(R.string.region_mismatch_error)
        } else {
            originalDescriptionText
        }
    }

}
