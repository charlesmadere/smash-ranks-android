package com.garpr.android.features.activityRequirements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.garpr.android.R
import com.garpr.android.data.models.RankingCriteria
import com.garpr.android.dialogs.BaseBottomSheetDialogFragment
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.extensions.requireString
import kotlinx.android.synthetic.main.dialog_activity_requirements.*
import java.text.NumberFormat

class ActivityRequirementsDialogFragment : BaseBottomSheetDialogFragment() {

    private val numberFormat = NumberFormat.getIntegerInstance()
    private val rankingCriteria by lazy { arguments.requireParcelable<RankingCriteria>(KEY_RANKING_CRITERIA) }
    private val regionDisplayName by lazy { arguments.requireString(KEY_REGION_DISPLAY_NAME) }


    companion object {
        const val TAG = "ActivityRequirementsDialogFragment"
        private const val KEY_RANKING_CRITERIA = "RankingCriteria"
        private const val KEY_REGION_DISPLAY_NAME = "RegionDisplayName"

        fun create(regionDisplayName: String, rankingCriteria: RankingCriteria): ActivityRequirementsDialogFragment {
            val args = Bundle()
            args.putString(KEY_REGION_DISPLAY_NAME, regionDisplayName)
            args.putParcelable(KEY_RANKING_CRITERIA, rankingCriteria)

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

        val rankingNumTourneysAttended = rankingCriteria.rankingNumTourneysAttended
        val rankingActivityDayLimit = rankingCriteria.rankingActivityDayLimit

        if (rankingNumTourneysAttended == null || rankingActivityDayLimit == null) {
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
