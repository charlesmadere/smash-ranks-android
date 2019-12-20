package com.garpr.android.features.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.garpr.android.R
import com.garpr.android.data.models.SmashRosterSyncResult
import com.garpr.android.features.common.views.SimplePreferenceView

class SmashRosterSyncPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), View.OnClickListener {

    var listener: Listener? = null

    interface Listener {
        fun onClick(v: SmashRosterSyncPreferenceView)
    }

    init {
        titleText = context.getText(R.string.smash_roster_sync_status)
        setLoading()
        setOnClickListener(this)
    }

    override fun onClick(v: View) {
        listener?.onClick(this)
    }

    fun setContent(syncResult: SmashRosterSyncResult?) {
        isEnabled = true

        descriptionText = if (syncResult == null) {
            resources.getText(R.string.sync_has_yet_to_occur)
        } else if (syncResult.success) {
            resources.getString(R.string.last_sync_x,
                    syncResult.date.getRelativeDateTimeText(context))
        } else if (syncResult.message.isNullOrBlank()) {
            resources.getString(R.string.sync_error_occurred_code, syncResult.httpCode)
        } else if (syncResult.httpCode == null) {
            resources.getString(R.string.sync_error_occurred_x, syncResult.message)
        } else {
            resources.getString(R.string.sync_error_occurred_x_code, syncResult.message,
                    syncResult.httpCode)
        }
    }

    fun setLoading() {
        isEnabled = false
        descriptionText = resources.getText(R.string.loading_sync_status_)
    }

    fun setSyncing() {
        descriptionText = resources.getText(R.string.syncing_now_)
    }

}
