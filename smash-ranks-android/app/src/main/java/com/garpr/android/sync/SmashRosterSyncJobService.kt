package com.garpr.android.sync

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.garpr.android.extensions.appComponent
import com.garpr.android.misc.Timber
import javax.inject.Inject

class SmashRosterSyncJobService : JobService(), SmashRosterSyncManager.OnSyncListeners {

    private var jobParameters: JobParameters? = null

    @Inject
    protected lateinit var smashRosterSyncManager: SmashRosterSyncManager

    @Inject
    protected lateinit var timber: Timber


    companion object {
        private const val TAG = "SmashRosterSyncJobService"
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    override fun onSmashRosterSyncBegin(smashRosterSyncManager: SmashRosterSyncManager) {
        timber.d(TAG, "sync beginning")
    }

    override fun onSmashRosterSyncComplete(smashRosterSyncManager: SmashRosterSyncManager) {
        val success: Boolean = smashRosterSyncManager.syncResult?.success == true
        timber.d(TAG, "sync complete... was successful: $success")
        jobParameters?.let { jobFinished(it, !success) }
    }

    override fun onStartJob(job: JobParameters): Boolean {
        timber.d(TAG, "starting job...")
        jobParameters = job

        smashRosterSyncManager.addListener(this)
        smashRosterSyncManager.sync()

        return true
    }

    override fun onStopJob(job: JobParameters): Boolean {
        val success: Boolean = smashRosterSyncManager.syncResult?.success == true
        timber.d(TAG, "stopping job... was successful: $success")
        smashRosterSyncManager.removeListener(this)
        return !success
    }

}
