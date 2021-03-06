package com.garpr.android.sync.roster

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.garpr.android.data.models.SmashRosterSyncResult
import com.garpr.android.misc.Timber
import org.koin.core.KoinComponent
import org.koin.core.inject

class SmashRosterSyncWorker(
        context: Context,
        workerParams: WorkerParameters
) : Worker(context, workerParams), KoinComponent {

    protected val smashRosterSyncManager: SmashRosterSyncManager by inject()
    protected val timber: Timber by inject()

    companion object {
        private const val TAG = "SmashRosterSyncWorker"
    }

    override fun doWork(): Result {
        timber.d(TAG, "work starting...")

        var syncResult: SmashRosterSyncResult? = null

        try {
            smashRosterSyncManager.sync()
                    .blockingAwait()

            timber.d(TAG, "successfully synced smash roster")
            syncResult = smashRosterSyncManager.syncResult
        } catch (e: RuntimeException) {
            timber.e(TAG, "Exception when syncing smash roster", e)
        }

        timber.d(TAG, "work complete")

        return if (syncResult?.success == true) {
            timber.d(TAG, "work was successful ($syncResult)")
            Result.success()
        } else {
            timber.w(TAG, "work wasn't successful, will retry ($syncResult)")
            Result.retry()
        }
    }

}
