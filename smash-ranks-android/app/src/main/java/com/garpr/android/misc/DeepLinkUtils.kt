package com.garpr.android.misc

import android.content.Context
import android.content.Intent
import android.net.Uri

import com.garpr.android.models.Endpoint
import com.garpr.android.models.Region
import com.garpr.android.models.RegionsBundle

interface DeepLinkUtils {

    fun buildIntentStack(context: Context, intent: Intent?, region: Region): List<Intent>?

    fun buildIntentStack(context: Context, uri: String?, region: Region): List<Intent>?

    fun buildIntentStack(context: Context, uri: Uri?, region: Region): List<Intent>?

    fun getEndpoint(intent: Intent?): Endpoint?

    fun getEndpoint(uri: String?): Endpoint?

    fun getEndpoint(uri: Uri?): Endpoint?

    fun getRegion(intent: Intent?, regionsBundle: RegionsBundle?): Region?

    fun getRegion(uri: String?, regionsBundle: RegionsBundle?): Region?

    fun getRegion(uri: Uri?, regionsBundle: RegionsBundle?): Region?

    fun isValidUri(intent: Intent?): Boolean

    fun isValidUri(uri: String?): Boolean

    fun isValidUri(uri: Uri?): Boolean

}
