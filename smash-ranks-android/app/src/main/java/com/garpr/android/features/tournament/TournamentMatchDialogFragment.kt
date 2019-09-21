package com.garpr.android.features.tournament

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.garpr.android.R
import com.garpr.android.data.models.FullTournament
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.features.common.fragments.dialogs.BaseBottomSheetDialogFragment
import com.garpr.android.features.headToHead.HeadToHeadActivity
import com.garpr.android.features.player.PlayerActivity
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.dialog_tournament_match.*
import org.koin.android.ext.android.inject

class TournamentMatchDialogFragment : BaseBottomSheetDialogFragment() {

    private val match by lazy { arguments.requireParcelable<FullTournament.Match>(KEY_MATCH) }

    protected val regionRepository: RegionRepository by inject()

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
                    regionRepository.getRegion(requireContext())))
            dismissAllowingStateLoss()
        }

        dialogSecondPlayer.text = match.loserName
        dialogSecondPlayer.setOnClickListener {
            startActivity(PlayerActivity.getLaunchIntent(requireContext(), match.loserId,
                    regionRepository.getRegion(requireContext())))
            dismissAllowingStateLoss()
        }

        dialogHeadToHead.setOnClickListener {
            startActivity(HeadToHeadActivity.getLaunchIntent(requireContext(), match,
                    regionRepository.getRegion(requireContext())))
            dismissAllowingStateLoss()
        }
    }

}
