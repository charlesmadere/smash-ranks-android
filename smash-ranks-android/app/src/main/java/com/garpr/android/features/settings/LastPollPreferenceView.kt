package com.garpr.android.features.settings

import android.content.Context
import android.util.AttributeSet
import com.garpr.android.R
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.features.common.views.SimplePreferenceView

class LastPollPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs) {

    init {
        isEnabled = false
        titleText = context.getText(R.string.last_poll)
        descriptionText = context.getText(R.string.poll_has_yet_to_occur)
    }

    fun setContent(lastPoll: SimpleDate) {
        descriptionText = lastPoll.getRelativeDateTimeText(context)
    }

}
