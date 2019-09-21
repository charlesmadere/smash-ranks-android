package com.garpr.android.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.garpr.android.R
import com.garpr.android.extensions.optInt
import com.garpr.android.extensions.optPutInt
import com.garpr.android.extensions.requireString
import com.garpr.android.features.common.fragments.dialogs.BaseBottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_activity_requirements.*
import java.text.NumberFormat

class ActivityRequirementsDialogFragment : BaseBottomSheetDialogFragment() {

    private val numberFormat = NumberFormat.getIntegerInstance()
    private val rankingActivityDayLimit: Int? by lazy { arguments.optInt(KEY_RANKING_ACTIVITY_DAY_LIMIT) }
    private val rankingNumTourneysAttended: Int? by lazy { arguments.optInt(KEY_RANKING_NUM_TOURNEYS_ATTENDED) }
    private val regionDisplayName by lazy { arguments.requireString(KEY_REGION_DISPLAY_NAME) }

    companion object {
        const val TAG = "ActivityRequirementsDialogFragment"
        private const val KEY_RANKING_ACTIVITY_DAY_LIMIT = "RankingActivityDayLimit"
        private const val KEY_RANKING_NUM_TOURNEYS_ATTENDED = "RankingNumTourneysAttended"
        private const val KEY_REGION_DISPLAY_NAME = "RegionDisplayName"

        fun create(rankingActivityDayLimit: Int?, rankingNumTourneysAttended: Int?,
                regionDisplayName: String): ActivityRequirementsDialogFragment {
            val args = Bundle()
            args.optPutInt(KEY_RANKING_ACTIVITY_DAY_LIMIT, rankingActivityDayLimit)
            args.optPutInt(KEY_RANKING_NUM_TOURNEYS_ATTENDED, rankingNumTourneysAttended)
            args.putString(KEY_REGION_DISPLAY_NAME, regionDisplayName)

            val fragment = ActivityRequirementsDialogFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_activity_requirements, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogActivityRequirementsHead.text = getString(R.string.x_activity_requirements,
                regionDisplayName)

        val rankingActivityDayLimit = this.rankingActivityDayLimit
        val rankingNumTourneysAttended = this.rankingNumTourneysAttended

        if (rankingActivityDayLimit == null || rankingNumTourneysAttended == null) {
            dialogActivityRequirementsBody.setText(R.string.unknown_activity_requirements)
            return
        }

        val tournaments = resources.getQuantityString(R.plurals.x_tournaments,
                rankingNumTourneysAttended, numberFormat.format(rankingNumTourneysAttended))
        val days = resources.getQuantityString(R.plurals.x_days, rankingActivityDayLimit,
                numberFormat.format(rankingActivityDayLimit))
        dialogActivityRequirementsBody.text = getString(R.string.x_within_the_last_y, tournaments,
                days)
    }

}
