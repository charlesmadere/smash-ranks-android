package com.garpr.android.features.settings

import android.content.Context
import android.util.AttributeSet
import com.garpr.android.R
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.extensions.appComponent
import com.garpr.android.features.common.views.SimplePreferenceView
import com.garpr.android.preferences.Preference
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import javax.inject.Inject

class LastPollPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), Preference.OnPreferenceChangeListener<SimpleDate> {

    @Inject
    protected lateinit var rankingsPollingPreferenceStore: RankingsPollingPreferenceStore


    init {
        isEnabled = false
        titleText = context.getText(R.string.last_poll)

        if (isInEditMode) {
            descriptionText = context.getText(R.string.poll_has_yet_to_occur)
        } else {
            appComponent.inject(this)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        rankingsPollingPreferenceStore.lastPoll.addListener(this)
        refresh()
    }

    override fun onDetachedFromWindow() {
        rankingsPollingPreferenceStore.lastPoll.removeListener(this)
        super.onDetachedFromWindow()
    }

    override fun onPreferenceChange(preference: Preference<SimpleDate>) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        val date = rankingsPollingPreferenceStore.lastPoll.get()
        descriptionText = date?.getRelativeDateTimeText(context) ?:
                context.getText(R.string.poll_has_yet_to_occur)
    }

}
