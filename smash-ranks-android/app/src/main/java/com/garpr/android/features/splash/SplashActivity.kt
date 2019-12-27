package com.garpr.android.features.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.garpr.android.R
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.features.home.HomeActivity
import com.garpr.android.features.setIdentity.SetIdentityActivity
import com.garpr.android.features.setRegion.SetRegionActivity
import com.garpr.android.misc.RequestCodes
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity(), SplashCardView.Listeners {

    private var contentViewSet = false
    override val activityName = TAG

    private val viewModel: SplashScreenViewModel by viewModel()

    protected val regionRepository: RegionRepository by inject()

    companion object {
        private const val TAG = "SplashActivity"

        fun getLaunchIntent(context: Context): Intent {
            val intent = Intent(context, SplashActivity::class.java)
            return Intent.makeRestartActivityTask(intent.component)
        }
    }

    private fun initListeners() {
        viewModel.stateLiveData.observe(this, Observer {
            refreshState(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListeners()
    }

    override fun onCustomizeIdentityClick(v: SplashCardView) {
        startActivityForResult(SetIdentityActivity.getLaunchIntent(this),
                RequestCodes.CHANGE_IDENTITY.value)
    }

    override fun onCustomizeRegionClick(v: SplashCardView) {
        startActivityForResult(SetRegionActivity.getLaunchIntent(this),
                RequestCodes.CHANGE_REGION.value)
    }

    override fun onRemoveIdentityClick(v: SplashCardView) {
        viewModel.removeIdentity()
    }

    override fun onStartUsingTheAppClick(v: SplashCardView) {
        viewModel.setSplashScreenComplete()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        splashCardView.listeners = this
    }

    private fun refreshState(state: SplashScreenViewModel.State) {
        if (state.isSplashScreenComplete) {
            startHomeActivity()
            return
        }

        if (!contentViewSet) {
            contentViewSet = true
            setContentView(R.layout.activity_splash)
        }

        splashCardView.setContent(state.identity, state.region)
    }

    private fun startHomeActivity() {
        timber.d(TAG, "starting ${HomeActivity.TAG}...")
        startActivity(HomeActivity.getLaunchIntent(context = this))
        finish()
    }

}
