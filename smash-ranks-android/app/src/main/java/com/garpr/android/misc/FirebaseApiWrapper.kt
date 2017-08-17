package com.garpr.android.misc

import com.firebase.jobdispatcher.FirebaseJobDispatcher

interface FirebaseApiWrapper {

    fun getJobDispatcher(): FirebaseJobDispatcher

}
