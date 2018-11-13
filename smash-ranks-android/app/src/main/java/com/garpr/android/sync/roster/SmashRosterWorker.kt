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
) : Worker(context, workerParams) {

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
        timber.d(TAG, "work starting...")

        smashRosterSyncManager.sync()

        val syncResult = smashRosterSyncManager.syncResult
        timber.d(TAG, "work complete")

        return if (syncResult?.success == true) {
            timber.d(TAG, "work was successful: $syncResult")
            Result.SUCCESS
        } else {
            timber.d(TAG, "work wasn't successful, will retry: $syncResult")
            Result.RETRY
        }
    }

}
