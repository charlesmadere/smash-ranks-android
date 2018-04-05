package com.garpr.android.managers

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.garpr.android.misc.Timber

class FacebookFrescoManager(
        private val application: Application,
        private val timber: Timber
) : ImageLibraryManager {

    companion object {
        private const val TAG = "FacebookFrescoManager"
    }

    @Synchronized
    override fun initialize() {
        if (Fresco.hasBeenInitialized()) {
            timber.w(TAG, "Facebook Fresco has already been initialized!")
        } else {
            Fresco.initialize(application)
        }
    }

}
