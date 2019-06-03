package com.garpr.android.features.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.garpr.android.R
import com.garpr.android.extensions.appComponent
import com.garpr.android.features.base.BaseActivity
import com.garpr.android.features.home.HomeActivity
import com.garpr.android.managers.SplashScreenManager
import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashCardView.Listener {

    @Inject
    protected lateinit var splashScreenManager: SplashScreenManager


    companion object {
        private const val TAG = "SplashActivity"

        fun getLaunchIntent(context: Context): Intent {
            val intent = Intent(context, SplashActivity::class.java)
            return Intent.makeRestartActivityTask(intent.component)
        }
    }

    override val activityName = TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        if (splashScreenManager.showSplashScreen) {
            timber.d(TAG, "showing $TAG...")
            setContentView(R.layout.activity_splash)
        } else {
            timber.d(TAG, "skipping $TAG...")
            startHomeActivity()
        }
    }

    override fun onStartUsingTheAppClick(v: SplashCardView) {
        startHomeActivity()
    }

    private fun startHomeActivity() {
        timber.d(TAG, "starting ${HomeActivity.TAG}...")

        splashScreenManager.setSplashScreenComplete()
        startActivity(HomeActivity.getLaunchIntent(context = this))
        finish()
    }

}
