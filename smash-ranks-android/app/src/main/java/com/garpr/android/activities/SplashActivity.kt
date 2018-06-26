package com.garpr.android.activities

import android.os.Bundle
import com.garpr.android.R
import com.garpr.android.views.SplashCardView

class SplashActivity : BaseActivity(), SplashCardView.Listener {

    companion object {
        private const val TAG = "SplashActivity"
    }

    override val activityName = TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (generalPreferenceStore.hajimeteKimasu.get() == true) {
            timber.d(TAG, "showing SplashActivity...")
            setContentView(R.layout.activity_splash)
        } else {
            startHomeActivity()
        }
    }

    override fun onStartUsingTheAppClick(v: SplashCardView) {
        startHomeActivity()
    }

    private fun startHomeActivity() {
        timber.d(TAG, "starting HomeActivity...")

        generalPreferenceStore.hajimeteKimasu.set(false)
        startActivity(HomeActivity.getLaunchIntent(context = this))
        finish()
    }

}
