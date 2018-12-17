package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.garpr.android.R
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.extensions.subtitle
import com.garpr.android.managers.RegionManager
import com.garpr.android.models.Region
import javax.inject.Inject

class TournamentsActivity : BaseActivity() {

    @Inject
    protected lateinit var regionManager: RegionManager


    companion object {
        private const val TAG = "TournamentsActivity"

        fun getLaunchIntent(context: Context, region: Region? = null): Intent {
            return Intent(context, TournamentsActivity::class.java)
                    .putOptionalExtra(EXTRA_REGION, region)
        }
    }

    override val activityName = TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(R.layout.activity_tournaments)
        subtitle = regionManager.getRegion(this).displayName
    }

}
