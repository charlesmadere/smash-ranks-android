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

        generalPreferenceStore.hajimeteKimasu.set(false)
        startActivity(HomeActivity.getLaunchIntent(context = this))
        finish()
    }

}
