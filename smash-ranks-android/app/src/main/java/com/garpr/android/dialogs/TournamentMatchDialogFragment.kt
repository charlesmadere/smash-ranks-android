package com.garpr.android.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.garpr.android.R
import com.garpr.android.activities.PlayerActivity
import com.garpr.android.data.models.FullTournament
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.features.headToHead.HeadToHeadActivity
import com.garpr.android.managers.RegionManager
import kotlinx.android.synthetic.main.dialog_tournament_match.*
import javax.inject.Inject

class TournamentMatchDialogFragment : BaseBottomSheetDialogFragment() {

    @Inject
    protected lateinit var regionManager: RegionManager

    private val match by lazy { arguments.requireParcelable<FullTournament.Match>(KEY_MATCH) }


    companion object {
        const val TAG = "TournamentMatchDialogFragment"
        private const val KEY_MATCH = "Match"

        fun create(match: FullTournament.Match): TournamentMatchDialogFragment {
            val args = Bundle()
            args.putParcelable(KEY_MATCH, match)

            val fragment = TournamentMatchDialogFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_tournament_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogFirstPlayer.text = match.winnerName
        dialogFirstPlayer.setOnClickListener {
            startActivity(PlayerActivity.getLaunchIntent(requireContext(), match.winnerId,
                    regionManager.getRegion(requireContext())))
            dismissAllowingStateLoss()
        }

        dialogSecondPlayer.text = match.loserName
        dialogSecondPlayer.setOnClickListener {
            startActivity(PlayerActivity.getLaunchIntent(requireContext(), match.loserId,
                    regionManager.getRegion(requireContext())))
            dismissAllowingStateLoss()
        }

        dialogHeadToHead.setOnClickListener {
            startActivity(HeadToHeadActivity.getLaunchIntent(requireContext(), match,
                    regionManager.getRegion(requireContext())))
            dismissAllowingStateLoss()
        }
    }

}
