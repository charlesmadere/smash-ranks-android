package com.garpr.android.wrappers

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.garpr.android.misc.Timber

class FacebookFrescoWrapper(
        private val application: Application,
        private val timber: Timber
) : ImageLibraryWrapper {

    companion object {
        private const val TAG = "FacebookFrescoWrapper"
    }

    override fun initialize() {
        if (Fresco.hasBeenInitialized()) {
            timber.w(TAG, "Fresco has already been initialized!")
        } else {
            timber.d(TAG, "Initializing Fresco...")
            Fresco.initialize(application)
            timber.d(TAG, "Fresco has been initialized")
        }
    }

}
