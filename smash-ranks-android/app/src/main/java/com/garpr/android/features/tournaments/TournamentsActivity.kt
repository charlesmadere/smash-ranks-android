package com.garpr.android.features.tournaments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.garpr.android.R
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.features.base.BaseActivity
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.activity_tournaments.*
import javax.inject.Inject

class TournamentsActivity : BaseActivity() {

    @Inject
    protected lateinit var regionRepository: RegionRepository


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
        toolbar.subtitleText = regionRepository.getRegion(this).displayName
    }

}
