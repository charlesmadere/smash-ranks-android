package com.garpr.android.features.tournaments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.garpr.android.R
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.activity_tournaments.*
import org.koin.android.ext.android.inject

class TournamentsActivity : BaseActivity() {

    protected val regionRepository: RegionRepository by inject()

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
        setContentView(R.layout.activity_tournaments)
    }

    override fun onViewsBound() {
        super.onViewsBound()

        toolbar.subtitleText = regionRepository.getRegion(this).displayName
    }

}
