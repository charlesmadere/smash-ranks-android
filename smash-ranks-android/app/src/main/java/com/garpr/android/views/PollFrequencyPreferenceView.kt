package com.garpr.android.views

import android.content.Context
import android.content.DialogInterface
import android.support.annotation.AttrRes
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.view.View
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.models.PollFrequency
import com.garpr.android.preferences.Preference
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import javax.inject.Inject

class PollFrequencyPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0
) : SimplePreferenceView(context, attrs, defStyleAttr), DialogInterface.OnClickListener,
        Preference.OnPreferenceChangeListener<PollFrequency>, View.OnClickListener {

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

        val current = rankingsPollingPreferenceStore.pollFrequency.get()
        val selected = PollFrequency.values()[which]

        if (current == selected) {
            return
        }

        rankingsPollingPreferenceStore.pollFrequency.set(selected)
        refresh()
    }

    override fun onClick(v: View) {
        val items = arrayOfNulls<CharSequence>(PollFrequency.values().size)

        for (i in 0 until PollFrequency.values().size) {
            items[i] = resources.getText(PollFrequency.values()[i].textResId)
        }

        val current = rankingsPollingPreferenceStore.pollFrequency.get()
        val checkedItem = current?.ordinal ?: -1

        AlertDialog.Builder(context)
                .setSingleChoiceItems(items, checkedItem, this)
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
            App.get().appComponent.inject(this)
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

        val pollFrequency = rankingsPollingPreferenceStore.pollFrequency.get()
        descriptionText = resources.getText(pollFrequency?.textResId ?: R.string.not_yet_set)
    }

}
