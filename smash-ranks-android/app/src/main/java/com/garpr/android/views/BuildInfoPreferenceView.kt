package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import com.garpr.android.BuildConfig
import com.garpr.android.R

class BuildInfoPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs) {

    override fun onFinishInflate() {
        super.onFinishInflate()

        isEnabled = false
        titleText = resources.getString(R.string.version_x, BuildConfig.VERSION_NAME)
        descriptionText = resources.getString(R.string.build_x_y, BuildConfig.VERSION_CODE,
                BuildConfig.VERSION_NAME)
    }

}
