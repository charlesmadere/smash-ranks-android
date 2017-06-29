package com.garpr.android.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.misc.DeepLinkUtils
import com.garpr.android.models.Region
import com.garpr.android.models.RegionsBundle
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import javax.inject.Inject

class DeepLinkActivity : BaseActivity(), ApiListener<RegionsBundle> {

    @Inject
    lateinit internal var mDeepLinkUtils: DeepLinkUtils

    @Inject
    lateinit internal var mServerApi: ServerApi


    companion object {
        private val TAG = "DeepLinkActivity"
    }

    private fun deepLink(region: Region) {
        val intentStack = mDeepLinkUtils.buildIntentStack(this, intent, region)

        if (intentStack == null || intentStack.isEmpty()) {
            startActivity(HomeActivity.getLaunchIntent(this))
        } else {
            ContextCompat.startActivities(this, intentStack)
        }

        supportFinishAfterTransition()
    }

    private fun error() {
        Toast.makeText(this, R.string.error_loading_deep_link_data, Toast.LENGTH_LONG).show()
        startActivity(HomeActivity.getLaunchIntent(this))
        supportFinishAfterTransition()
    }

    override fun failure(errorCode: Int) {
        error()
    }

    override fun getActivityName(): String {
        return TAG
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_deep_link)

        if (mDeepLinkUtils.isValidUri(intent)) {
            mServerApi.getRegions(ApiCall(this))
        } else {
            error()
        }
    }

    override fun success(regionsBundle: RegionsBundle?) {
        if (regionsBundle != null && regionsBundle.hasRegions()) {
            val region = mDeepLinkUtils.getRegion(intent, regionsBundle)

            if (region == null) {
                error()
            } else {
                deepLink(region)
            }
        } else {
            error()
        }
    }

}
