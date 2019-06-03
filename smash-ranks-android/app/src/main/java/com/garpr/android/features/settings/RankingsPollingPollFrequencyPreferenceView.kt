package com.garpr.android.features.settings

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.garpr.android.R
import com.garpr.android.data.models.PollFrequency
import com.garpr.android.extensions.appComponent
import com.garpr.android.features.common.SimplePreferenceView
import com.garpr.android.features.sync.rankings.RankingsPollingManager
import com.garpr.android.preferences.Preference
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import javax.inject.Inject

class RankingsPollingPollFrequencyPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), DialogInterface.OnClickListener,
        Preference.OnPreferenceChangeListener<PollFrequency>, View.OnClickListener {

    @Inject
    protected lateinit var rankingsPollingManager: RankingsPollingManager

    @Inject
    protected lateinit var rankingsPollingPreferenceStore: RankingsPollingPreferenceStore


    init {
        titleText = context.getText(R.string.poll_frequency)
        setOnClickListener(this)

        if (isInEditMode) {
            descriptionText = context.getText(R.string.every_3_days)
        } else {
            appComponent.inject(this)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        rankingsPollingPreferenceStore.pollFrequency.addListener(this)
        refresh()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        dialog.dismiss()

        val current = rankingsPollingManager.pollFrequency
        val selected = PollFrequency.values()[which]

        if (current == selected) {
            return
        }

        rankingsPollingManager.pollFrequency = selected
        refresh()
    }

    override fun onClick(v: View) {
        val items = arrayOfNulls<CharSequence>(PollFrequency.values().size)

        for (i in 0 until PollFrequency.values().size) {
            items[i] = resources.getText(PollFrequency.values()[i].textResId)
        }

        val pollFrequency = rankingsPollingManager.pollFrequency

        AlertDialog.Builder(context)
                .setSingleChoiceItems(items, pollFrequency.ordinal, this)
                .setTitle(R.string.poll_frequency)
                .show()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        rankingsPollingPreferenceStore.pollFrequency.removeListener(this)
    }

    override fun onPreferenceChange(preference: Preference<PollFrequency>) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        val pollFrequency = rankingsPollingManager.pollFrequency
        descriptionText = context.getText(pollFrequency.textResId)
    }

}
