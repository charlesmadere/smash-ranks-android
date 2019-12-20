package com.garpr.android.features.settings

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.garpr.android.R
import com.garpr.android.data.models.PollFrequency
import com.garpr.android.features.common.views.SimplePreferenceView

class PollFrequencyPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), DialogInterface.OnClickListener, View.OnClickListener {

    var listener: Listener? = null
    private var pollFrequency: PollFrequency? = null

    interface Listener {
        fun onPollFrequencyChange(v: PollFrequencyPreferenceView, pollFrequency: PollFrequency)
    }

    init {
        titleText = context.getText(R.string.poll_frequency)
        setOnClickListener(this)

        if (isInEditMode) {
            descriptionText = context.getText(R.string.every_3_days)
        }
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        dialog.dismiss()

        val selected = PollFrequency.values()[which]

        if (pollFrequency != selected) {
            listener?.onPollFrequencyChange(this, selected)
        }
    }

    override fun onClick(v: View) {
        val items = PollFrequency.values()
                .map { resources.getText(it.textResId) }
                .toTypedArray()

        val checkedItem = checkNotNull(pollFrequency).ordinal

        AlertDialog.Builder(context)
                .setSingleChoiceItems(items, checkedItem, this)
                .setTitle(R.string.poll_frequency)
                .show()
    }

    fun setContent(pollFrequency: PollFrequency) {
        this.pollFrequency = pollFrequency
        descriptionText = resources.getText(pollFrequency.textResId)
    }

}
