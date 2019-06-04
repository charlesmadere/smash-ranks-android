package com.garpr.android.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.garpr.android.R
import com.garpr.android.extensions.appComponent
import com.garpr.android.features.base.BaseBottomSheetDialogFragment
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.ShareUtils
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.dialog_share_region.*
import javax.inject.Inject

class ShareRegionDialogFragment : BaseBottomSheetDialogFragment(), Refreshable,
        RegionRepository.OnRegionChangeListener {

    @Inject
    protected lateinit var regionRepository: RegionRepository

    @Inject
    protected lateinit var shareUtils: ShareUtils


    companion object {
        const val TAG = "ShareRegionDialogFragment"
        fun create() = ShareRegionDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_share_region, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        regionRepository.removeListener(this)
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

        dialogRegionRankings.setOnClickListener {
            shareUtils.shareRankings(requireActivity())
            dismissAllowingStateLoss()
        }

        dialogRegionTournaments.setOnClickListener {
            shareUtils.shareTournaments(requireActivity())
            dismissAllowingStateLoss()
        }

        regionRepository.addListener(this)
    }

    override fun refresh() {
        val region = regionRepository.getRegion(context)
        dialogRegionRankings.text = getString(R.string.x_rankings, region.displayName)
        dialogRegionTournaments.text = getString(R.string.x_tournaments, region.displayName)
    }

}
