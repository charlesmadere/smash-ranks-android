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

        descriptionText = if (BuildConfig.DEBUG) {
            resources.getString(R.string.build_x_debug, BuildConfig.VERSION_CODE)
        } else {
            resources.getString(R.string.build_x, BuildConfig.VERSION_CODE)
        }
    }

}
