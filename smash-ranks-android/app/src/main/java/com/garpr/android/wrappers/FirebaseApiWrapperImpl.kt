package com.garpr.android.wrappers

import android.app.Application
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver

class FirebaseApiWrapperImpl(
        private val application: Application
) : FirebaseApiWrapper {

    override val jobDispatcher: FirebaseJobDispatcher by lazy { FirebaseJobDispatcher(GooglePlayDriver(application)) }

}
