package com.garpr.android.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.TaskStackBuilder
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.appComponent
import com.garpr.android.managers.RegionManager.RegionHandle
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.GeneralPreferenceStore
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), Heartbeat, RegionHandle {

    @Inject
    protected lateinit var generalPreferenceStore: GeneralPreferenceStore

    @Inject
    protected lateinit var timber: Timber


    companion object {
        private const val TAG = "BaseActivity"
        private val CNAME = BaseActivity::class.java.canonicalName
        internal val EXTRA_REGION = "$CNAME.Region"
    }

    protected abstract val activityName: String

    override val currentRegion: Region?
        get() = intent?.getParcelableExtra(EXTRA_REGION)

    override val isAlive: Boolean
        get() = !isFinishing && !isDestroyed

    open fun navigateUp() {
        val intent = supportParentActivityIntent

        if (intent == null) {
            supportFinishAfterTransition()
        } else if (supportShouldUpRecreateTask(intent)) {
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(intent)
                    .startActivities()
        } else {
            supportNavigateUpTo(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)

        generalPreferenceStore.nightMode.get()?.let {
            delegate.setLocalNightMode(it.themeValue)
        } ?: throw RuntimeException("nightMode is null")

        super.onCreate(savedInstanceState)
        timber.d(TAG, "$activityName created")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navigateUp()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    protected open fun onViewsBound() {
        // intentionally empty, children can override
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        onViewsBound()
    }

    override fun toString() = activityName

}
