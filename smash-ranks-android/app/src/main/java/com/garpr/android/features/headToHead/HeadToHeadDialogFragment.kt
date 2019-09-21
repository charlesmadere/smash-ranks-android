package com.garpr.android.features.headToHead

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.garpr.android.R
import com.garpr.android.data.models.HeadToHeadMatch
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.features.common.fragments.dialogs.BaseBottomSheetDialogFragment
import com.garpr.android.features.player.PlayerActivity
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.dialog_head_to_head.*
import org.koin.android.ext.android.inject

class HeadToHeadDialogFragment : BaseBottomSheetDialogFragment() {

    private val match by lazy { arguments.requireParcelable<HeadToHeadMatch>(KEY_MATCH) }

    protected val regionRepository: RegionRepository by inject()

    companion object {
        const val TAG = "HeadToHeadDialogFragment"
        private const val KEY_MATCH = "Match"

        fun create(match: HeadToHeadMatch): HeadToHeadDialogFragment {
            val args = Bundle()
            args.putParcelable(KEY_MATCH, match)

            val fragment = HeadToHeadDialogFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_head_to_head, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogFirstPlayer.text = match.player.name
        dialogFirstPlayer.setOnClickListener {
            startActivity(PlayerActivity.getLaunchIntent(requireContext(), match.player,
                    regionRepository.getRegion(requireContext())))
            dismissAllowingStateLoss()
        }

        dialogSecondPlayer.text = match.opponent.name
        dialogSecondPlayer.setOnClickListener {
            startActivity(PlayerActivity.getLaunchIntent(requireContext(), match.opponent,
                    regionRepository.getRegion(requireContext())))
            dismissAllowingStateLoss()
        }
    }

}
