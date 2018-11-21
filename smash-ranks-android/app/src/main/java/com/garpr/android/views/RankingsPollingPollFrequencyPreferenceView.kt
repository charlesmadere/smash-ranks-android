package com.garpr.android.views

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.garpr.android.R
import com.garpr.android.extensions.appComponent
import com.garpr.android.models.PollFrequency
import com.garpr.android.preferences.Preference
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import com.garpr.android.sync.rankings.RankingsPollingManager
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

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
            rankingsPollingPreferenceStore.pollFrequency.addListener(this)
        }

        setOnClickListener(this)
        titleText = resources.getText(R.string.poll_frequency)

        if (isInEditMode) {
            return
        }

        refresh()
    }

    override fun onPreferenceChange(preference: Preference<PollFrequency>) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        val pollFrequency = rankingsPollingManager.pollFrequency
        descriptionText = resources.getText(pollFrequency.textResId)
    }

}