package com.garpr.android.views

import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.extensions.getActivity
import com.garpr.android.misc.ResultCodes
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.Preference
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import javax.inject.Inject

class RingtonePreferenceView : SimplePreferenceView, Preference.OnPreferenceChangeListener<Uri>,
        View.OnClickListener {

    @Inject
    protected lateinit var rankingsPollingPreferenceStore: RankingsPollingPreferenceStore

    @Inject
    protected lateinit var timber: Timber


    companion object {
        private const val TAG = "RingtonePreferenceView"
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    fun onActivityResult(data: Intent?) {
        if (data == null || !data.hasExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)) {
            return
        }

        val pickedUri = data.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
        rankingsPollingPreferenceStore.ringtone.set(pickedUri)
        refresh()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        rankingsPollingPreferenceStore.ringtone.addListener(this)
        refresh()
    }

    override fun onClick(v: View) {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)

        rankingsPollingPreferenceStore.ringtone.get()?.let {
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, it)
        }

        val activity = context.getActivity()

        try {
            activity.startActivityForResult(intent, ResultCodes.RINGTONE_SELECTED.value)
        } catch (e: ActivityNotFoundException) {
            timber.e(TAG, "Unable to start ringtone picker Activity", e)
            Toast.makeText(context, R.string.unable_to_launch_ringtone_picker, Toast.LENGTH_LONG)
                    .show()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        rankingsPollingPreferenceStore.ringtone.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
            rankingsPollingPreferenceStore.ringtone.addListener(this)
        }

        setOnClickListener(this)
        titleText = resources.getText(R.string.ringtone)

        if (isInEditMode) {
            return
        }

        refresh()
    }

    override fun onPreferenceChange(preference: Preference<Uri>) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        val ringtone = rankingsPollingPreferenceStore.ringtone.get()?.let {
            RingtoneManager.getRingtone(context, it)
        }

        descriptionText = if (ringtone == null) {
            resources.getText(R.string.none)
        } else {
            ringtone.getTitle(context)
        }
    }

}
