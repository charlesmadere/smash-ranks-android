package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.garpr.android.R
import com.garpr.android.extensions.appComponent
import com.garpr.android.sync.SmashRosterSyncManager
import javax.inject.Inject

class SmashRosterSyncPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), SmashRosterSyncManager.OnSyncListeners,
        View.OnClickListener {

    @Inject
    protected lateinit var smashRosterSyncManager: SmashRosterSyncManager


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        smashRosterSyncManager.addListener(this)
        refresh()
    }

    override fun onClick(v: View) {
        if (!smashRosterSyncManager.isSyncing) {
            smashRosterSyncManager.sync()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        smashRosterSyncManager.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
            smashRosterSyncManager.addListener(this)
        }

        setOnClickListener(this)
        titleText = resources.getText(R.string.smash_roster_storage)

        if (isInEditMode) {
            descriptionText = resources.getText(R.string.sync_has_yet_to_occur)
        } else {
            refresh()
        }
    }

    override fun onSmashRosterSyncBegin(smashRosterSyncManager: SmashRosterSyncManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun onSmashRosterSyncComplete(smashRosterSyncManager: SmashRosterSyncManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        if (smashRosterSyncManager.isSyncing) {
            descriptionText = resources.getText(R.string.syncing_now_)
            return
        }

        val syncResult = smashRosterSyncManager.syncResult

        if (syncResult == null) {
            descriptionText = resources.getText(R.string.sync_has_yet_to_occur)
        } else if (syncResult.success) {
            descriptionText = resources.getString(R.string.last_sync_x,
                    syncResult.date.getRelativeDateTimeText(context))
        } else if (syncResult.message?.isNotBlank() == true) {
            descriptionText = if (syncResult.httpCode == null) {
                resources.getString(R.string.sync_error_occurred_x,
                        syncResult.message)
            } else {
                resources.getString(R.string.sync_error_occurred_x_code,
                        syncResult.message, syncResult.httpCode)
            }
        } else if (syncResult.httpCode == null) {
            descriptionText = resources.getText(R.string.sync_error_occurred)
        } else {
            descriptionText = resources.getString(R.string.sync_error_occurred_code,
                    syncResult.httpCode)
        }
    }

}
