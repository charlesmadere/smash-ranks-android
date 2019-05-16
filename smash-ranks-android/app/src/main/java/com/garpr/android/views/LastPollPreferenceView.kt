package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import com.garpr.android.R
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.extensions.appComponent
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
        titleText = context.getText(R.string.last_poll)

        if (isInEditMode) {
            descriptionText = context.getText(R.string.poll_has_yet_to_occur)
        }

        isEnabled = false
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
        super.onDetachedFromWindow()
        rankingsPollingPreferenceStore.lastPoll.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
            rankingsPollingPreferenceStore.lastPoll.addListener(this)
            refresh()
        }
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
