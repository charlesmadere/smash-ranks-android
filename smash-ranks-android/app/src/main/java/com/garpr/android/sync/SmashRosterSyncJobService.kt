package com.garpr.android.sync

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.garpr.android.extensions.appComponent
import com.garpr.android.misc.Timber
import com.garpr.android.networking.ApiListener
import javax.inject.Inject

class SmashRosterSyncJobService : JobService(), ApiListener<Boolean> {

    private var _isAlive: Boolean = true
    private var jobParameters: JobParameters? = null

    @Inject
    protected lateinit var timber: Timber


    companion object {
        private const val TAG = "SmashRosterSyncJobService"
    }

    override fun failure(errorCode: Int) {
        // TODO
    }

    override val isAlive: Boolean
        get() = _isAlive

    private fun jobFinished(needsReschedule: Boolean) {
        timber.d(TAG, "job finished... needs reschedule: $needsReschedule")
        jobParameters?.let { jobFinished(it, needsReschedule) }
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    override fun onStartJob(job: JobParameters): Boolean {
        timber.d(TAG, "starting job...")

        jobParameters = job

        // TODO
        return false
    }

    override fun onStopJob(job: JobParameters): Boolean {
        _isAlive = false
        timber.d(TAG, "stopping job... retry: ")

        // TODO
        return false
    }

    override fun success(`object`: Boolean?) {
        // TODO
    }

}
