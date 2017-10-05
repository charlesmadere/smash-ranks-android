package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import com.garpr.android.BuildConfig
import com.garpr.android.R

class BuildInfoPreferenceView : SimplePreferenceView {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()

        isEnabled = false
        titleText = resources.getString(R.string.version_x, BuildConfig.VERSION_NAME)

        descriptionText = if (BuildConfig.DEBUG) {
            resources.getString(R.string.build_x_debug, BuildConfig.VERSION_CODE)
        } else {
            resources.getString(R.string.build_x, BuildConfig.VERSION_CODE)
        }
    }

}
