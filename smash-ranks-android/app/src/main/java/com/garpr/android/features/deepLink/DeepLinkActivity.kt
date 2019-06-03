package com.garpr.android.features.deepLink

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.activities.BaseActivity
import com.garpr.android.features.home.HomeActivity
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.RegionsBundle
import com.garpr.android.extensions.appComponent
import com.garpr.android.misc.DeepLinkUtils
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import javax.inject.Inject

class DeepLinkActivity : BaseActivity(), ApiListener<RegionsBundle> {

    @Inject
    protected lateinit var deepLinkUtils: DeepLinkUtils

    @Inject
    protected lateinit var serverApi: ServerApi


    companion object {
        private const val TAG = "DeepLinkActivity"
    }

    override val activityName = TAG

    private fun deepLink(region: Region) {
        val intentStack = deepLinkUtils.buildIntentStack(this, intent, region)

        if (intentStack.isNullOrEmpty()) {
            startActivity(HomeActivity.getLaunchIntent(context = this))
        } else {
            ContextCompat.startActivities(this, intentStack.toTypedArray())
        }

        supportFinishAfterTransition()
    }

    private fun error() {
        Toast.makeText(this, R.string.error_loading_deep_link_data, Toast.LENGTH_LONG).show()
        startActivity(HomeActivity.getLaunchIntent(context = this))
        supportFinishAfterTransition()
    }

    override fun failure(errorCode: Int) {
        error()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(R.layout.activity_deep_link)

        if (deepLinkUtils.isValidUri(intent)) {
            serverApi.getRegions(listener = ApiCall(this))
        } else {
            error()
        }
    }

    override fun success(`object`: RegionsBundle?) {
        val region = deepLinkUtils.getRegion(intent, `object`)

        if (region == null) {
            error()
        } else {
            deepLink(region)
        }
    }

}
