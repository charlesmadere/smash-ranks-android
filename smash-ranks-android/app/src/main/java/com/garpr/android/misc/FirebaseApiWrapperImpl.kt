package com.garpr.android.misc

import android.app.Application
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver

class FirebaseApiWrapperImpl(
        private val mApplication: Application
) : FirebaseApiWrapper {

    override fun getJobDispatcher(): FirebaseJobDispatcher {
        return FirebaseJobDispatcher(GooglePlayDriver(mApplication))
    }

}
