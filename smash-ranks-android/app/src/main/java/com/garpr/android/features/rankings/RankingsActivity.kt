package com.garpr.android.features.rankings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.garpr.android.R
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.misc.RegionHandleUtils
import kotlinx.android.synthetic.main.activity_rankings.*
import org.koin.android.ext.android.inject

class RankingsActivity : BaseActivity() {

    protected val regionHandleUtils: RegionHandleUtils by inject()

    companion object {
        private const val TAG = "RankingsActivity"

        fun getLaunchIntent(context: Context, region: Region? = null): Intent {
            return Intent(context, RankingsActivity::class.java)
                    .putOptionalExtra(EXTRA_REGION, region)
        }
    }

    override val activityName = TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rankings)
    }

    override fun onViewsBound() {
        super.onViewsBound()
        toolbar.subtitleText = regionHandleUtils.getRegion(this).displayName
    }

}
