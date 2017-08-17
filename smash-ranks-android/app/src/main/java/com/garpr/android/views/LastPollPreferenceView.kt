package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.models.SimpleDate
import com.garpr.android.preferences.Preference
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import javax.inject.Inject

class LastPollPreferenceView : SimplePreferenceView,
        Preference.OnPreferenceChangeListener<SimpleDate> {

    @Inject
    lateinit protected var mRankingsPollingPreferenceStore: RankingsPollingPreferenceStore


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        mRankingsPollingPreferenceStore.lastPoll.addListener(this)
        refresh()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mRankingsPollingPreferenceStore.lastPoll.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
            mRankingsPollingPreferenceStore.lastPoll.addListener(this)
        }

        isEnabled = false
        setTitleText(R.string.last_poll)

        if (isInEditMode) {
            return
        }

        refresh()
    }

    override fun onPreferenceChange(preference: Preference<SimpleDate>) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        val date = mRankingsPollingPreferenceStore.lastPoll.get()

        if (date == null) {
            setDescriptionText(R.string.poll_has_yet_to_occur)
        } else {
            setDescriptionText(date.getRelativeDateTimeText(context))
        }
    }

}
