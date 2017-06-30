package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.view.View
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.models.PollFrequency
import com.garpr.android.preferences.Preference
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import javax.inject.Inject

class PollFrequencyPreferenceView : SimplePreferenceView, DialogInterface.OnClickListener,
        Preference.OnPreferenceChangeListener<PollFrequency>, View.OnClickListener {

    @Inject
    lateinit protected var mRankingsPollingPreferenceStore: RankingsPollingPreferenceStore


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        mRankingsPollingPreferenceStore.pollFrequency.addListener(this)
        refresh()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        dialog.dismiss()

        val current = mRankingsPollingPreferenceStore.pollFrequency.get()
        val selected = PollFrequency.values()[which]

        if (current == selected) {
            return
        }

        mRankingsPollingPreferenceStore.pollFrequency.set(selected)
        refresh()
    }

    override fun onClick(v: View) {
        val items = arrayOfNulls<CharSequence>(PollFrequency.values().size)

        for (i in 0..PollFrequency.values().size - 1) {
            items[i] = resources.getText(PollFrequency.values()[i].textResId)
        }

        val current = mRankingsPollingPreferenceStore.pollFrequency.get()
        val checkedItem = current?.ordinal ?: -1

        AlertDialog.Builder(context)
                .setSingleChoiceItems(items, checkedItem, this)
                .setTitle(R.string.poll_frequency)
                .show()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mRankingsPollingPreferenceStore.pollFrequency.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
            mRankingsPollingPreferenceStore.pollFrequency.addListener(this)
        }

        setOnClickListener(this)
        setTitleText(R.string.poll_frequency)

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

        val pollFrequency = mRankingsPollingPreferenceStore.pollFrequency.get()
        setDescriptionText(pollFrequency?.textResId ?: R.string.not_yet_set)
    }

}
