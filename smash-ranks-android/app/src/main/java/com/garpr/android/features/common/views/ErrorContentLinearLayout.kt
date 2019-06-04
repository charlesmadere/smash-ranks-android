package com.garpr.android.features.common.views

import android.content.Context
import android.util.AttributeSet
import com.garpr.android.R
import com.garpr.android.misc.Constants

class ErrorContentLinearLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : NoContentLinearLayout(context, attrs) {

    private val originalDescriptionText: CharSequence? = descriptionText


    fun setVisibility(visibility: Int, errorCode: Int) {
        super.setVisibility(visibility)

        descriptionText = if (errorCode == Constants.ERROR_CODE_BAD_REQUEST) {
            context.getText(R.string.region_mismatch_error)
        } else {
            originalDescriptionText
        }
    }

}
