package com.garpr.android.features.settings

import android.content.Context
import android.util.AttributeSet
import com.garpr.android.BuildConfig
import com.garpr.android.R
import com.garpr.android.views.SimplePreferenceView

class BuildInfoPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs) {

    init {
        isEnabled = false
        titleText = resources.getString(R.string.version_x, BuildConfig.VERSION_NAME)
        descriptionText = resources.getString(R.string.build_x_y, BuildConfig.VERSION_CODE,
                BuildConfig.BUILD_TYPE)
    }

}
