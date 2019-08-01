package com.garpr.android.features.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.garpr.android.R
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.appComponent
import com.garpr.android.features.common.views.SimplePreferenceView
import com.garpr.android.misc.RequestCodes
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.Preference
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import javax.inject.Inject

class RingtonePreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), Preference.OnPreferenceChangeListener<Uri>,
        View.OnClickListener {

    @Inject
    protected lateinit var rankingsPollingPreferenceStore: RankingsPollingPreferenceStore

    @Inject
    protected lateinit var timber: Timber


    companion object {
        private const val TAG = "RingtonePreferenceView"
    }

    init {
        titleText = context.getText(R.string.ringtone)
        setOnClickListener(this)

        if (isInEditMode) {
            descriptionText = context.getText(R.string.none)
        } else {
            appComponent.inject(this)
        }
    }

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

        val activity = this.activity

        try {
            if (activity == null) {
                context.startActivity(intent)
            } else {
                activity.startActivityForResult(intent, RequestCodes.CHANGE_RINGTONE.value)
            }
        } catch (e: ActivityNotFoundException) {
            timber.e(TAG, "Unable to start ringtone picker Activity", e)
            Toast.makeText(context, R.string.unable_to_launch_ringtone_picker, Toast.LENGTH_LONG)
                    .show()
        }
    }

    override fun onDetachedFromWindow() {
        rankingsPollingPreferenceStore.ringtone.removeListener(this)
        super.onDetachedFromWindow()
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
            context.getText(R.string.none)
        } else {
            ringtone.getTitle(context)
        }
    }

}
