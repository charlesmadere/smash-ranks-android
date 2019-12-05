package com.garpr.android.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.garpr.android.R
import com.garpr.android.features.common.fragments.dialogs.BaseBottomSheetDialogFragment
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.ShareUtils
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.dialog_share_region.*
import org.koin.android.ext.android.inject

class ShareRegionDialogFragment : BaseBottomSheetDialogFragment(), Refreshable,
        RegionRepository.OnRegionChangeListener {

    protected val regionRepository: RegionRepository by inject()
    protected val shareUtils: ShareUtils by inject()

    private val shareRankingsClickListener = View.OnClickListener {
        shareUtils.shareRankings(requireActivity())
        dismissAllowingStateLoss()
    }

    private val shareTournamentsClickListener = View.OnClickListener {
        shareUtils.shareTournaments(requireActivity())
        dismissAllowingStateLoss()
    }

    companion object {
        const val TAG = "ShareRegionDialogFragment"
        fun create() = ShareRegionDialogFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_share_region, container, false)
    }

    override fun onDestroyView() {
        regionRepository.removeListener(this)
        super.onDestroyView()
    }

    override fun onRegionChange(regionRepository: RegionRepository) {
        if (isAlive) {
            refresh()
        }
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogRegionRankings.setOnClickListener(shareRankingsClickListener)
        dialogRegionTournaments.setOnClickListener(shareTournamentsClickListener)
        regionRepository.addListener(this)
    }

    override fun refresh() {
        val region = regionRepository.getRegion(context)
        dialogRegionRankings.text = getString(R.string.x_rankings, region.displayName)
        dialogRegionTournaments.text = getString(R.string.x_tournaments, region.displayName)
    }

}
