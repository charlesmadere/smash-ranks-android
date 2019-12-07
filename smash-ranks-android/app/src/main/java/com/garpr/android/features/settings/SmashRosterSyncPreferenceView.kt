package com.garpr.android.features.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.garpr.android.R
import com.garpr.android.features.common.views.SimplePreferenceView
import com.garpr.android.misc.Schedulers
import com.garpr.android.sync.roster.SmashRosterSyncManager
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.inject

class SmashRosterSyncPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), KoinComponent, View.OnClickListener {

    private val disposables = CompositeDisposable()

    protected val schedulers: Schedulers by inject()
    protected val smashRosterSyncManager: SmashRosterSyncManager by inject()

    init {
        titleText = context.getText(R.string.smash_roster_sync_status)
        setOnClickListener(this)

        if (isInEditMode) {
            descriptionText = context.getText(R.string.sync_has_yet_to_occur)
        }
    }

    private fun initListeners() {
        disposables.add(smashRosterSyncManager.observeIsSyncing
                .subscribe {
                    post { refresh() }
                })
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        initListeners()
        refresh()
    }

    override fun onClick(v: View) {
        if (!smashRosterSyncManager.isSyncing) {
            disposables.add(smashRosterSyncManager.sync()
                    .subscribeOn(schedulers.background)
                    .subscribe())
        }
    }

    override fun onDetachedFromWindow() {
        disposables.clear()
        super.onDetachedFromWindow()
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
