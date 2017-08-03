package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.extensions.subtitle
import com.garpr.android.misc.RegionManager
import com.garpr.android.models.AbsRegion
import javax.inject.Inject

class TournamentsActivity : BaseActivity() {

    @Inject
    lateinit protected var mRegionManager: RegionManager


    companion object {
        private const val TAG = "TournamentsActivity"

        @JvmOverloads
        fun getLaunchIntent(context: Context, region: AbsRegion? = null): Intent {
            val intent = Intent(context, TournamentsActivity::class.java)

            if (region != null) {
                intent.putExtra(BaseActivity.EXTRA_REGION, region)
            }

            return intent
        }
    }

    override val activityName = TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_tournaments)
        subtitle = mRegionManager.getRegion(this).displayName
    }

    override val showUpNavigation = true

}
