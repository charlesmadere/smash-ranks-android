package com.garpr.android.wrappers

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.garpr.android.misc.Timber
import okhttp3.OkHttpClient

class FacebookFrescoWrapper(
        private val application: Application,
        private val okHttpClient: OkHttpClient,
        private val timber: Timber
) : ImageLibraryWrapper {

    companion object {
        private const val TAG = "FacebookFrescoWrapper"
    }


    override fun initialize() {
        if (Fresco.hasBeenInitialized()) {
            timber.w(TAG, "Fresco has already been initialized!")
            return
        }

        timber.d(TAG, "Initializing Fresco...")

        val config = OkHttpImagePipelineConfigFactory
                .newBuilder(application, okHttpClient)
                .build()

        Fresco.initialize(application, config)

        timber.d(TAG, "Fresco has been initialized")
    }

}
