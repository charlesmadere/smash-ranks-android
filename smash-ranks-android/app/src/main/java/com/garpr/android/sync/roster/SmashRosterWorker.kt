package com.garpr.android.sync.roster

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.garpr.android.extensions.appComponent
import com.garpr.android.misc.Timber
import javax.inject.Inject

class SmashRosterWorker(
        context: Context,
        workerParams: WorkerParameters
) : Worker(context, workerParams), SmashRosterSyncManager.OnSyncListeners {

    @Inject
    protected lateinit var smashRosterSyncManager: SmashRosterSyncManager

    @Inject
    protected lateinit var timber: Timber


    companion object {
        private const val TAG = "SmashRosterWorker"
    }

    init {
        context.appComponent.inject(this)
    }

    override fun doWork(): Result {
        timber.d(TAG, "starting work...")

        smashRosterSyncManager.addListener(this)
        smashRosterSyncManager.sync()

        return Result.SUCCESS
    }

    override fun onSmashRosterSyncBegin(smashRosterSyncManager: SmashRosterSyncManager) {
        timber.d(TAG, "sync beginning")
    }

    override fun onSmashRosterSyncComplete(smashRosterSyncManager: SmashRosterSyncManager) {
        timber.d(TAG, "sync complete... result: ${smashRosterSyncManager.syncResult}")
    }

}
