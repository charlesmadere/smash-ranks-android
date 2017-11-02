package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.extensions.subtitle
import com.garpr.android.misc.RegionManager
import com.garpr.android.models.Region
import javax.inject.Inject

class RankingsActivity : BaseActivity() {

    @Inject
    protected lateinit var mRegionManager: RegionManager


    companion object {
        private const val TAG = "RankingsActivity"

        fun getLaunchIntent(context: Context, region: Region? = null): Intent {
            val intent = Intent(context, RankingsActivity::class.java)

            if (region != null) {
                intent.putExtra(EXTRA_REGION, region)
            }

            return intent
        }
    }

    override val activityName = TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_rankings)
        subtitle = mRegionManager.getRegion(this).displayName
    }

    override val showUpNavigation = true

}
