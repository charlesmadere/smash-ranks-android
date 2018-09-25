package com.garpr.android.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.TaskStackBuilder
import com.garpr.android.R
import com.garpr.android.extensions.appComponent
import com.garpr.android.managers.RegionManager.RegionHandle
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.Timber
import com.garpr.android.models.Region
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.views.toolbars.MenuToolbar
import kotterknife.bindOptionalView
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), Heartbeat, RegionHandle {

    @Inject
    protected lateinit var generalPreferenceStore: GeneralPreferenceStore

    @Inject
    protected lateinit var timber: Timber

    protected val toolbar: Toolbar? by bindOptionalView(R.id.toolbar)


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

    protected open fun navigateUp() {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        (toolbar as? MenuToolbar)?.onCreateOptionsMenu(menuInflater, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navigateUp()
                return true
            }
        }

        if ((toolbar as? MenuToolbar)?.onOptionsItemSelected(item) == true) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        (toolbar as? MenuToolbar)?.refresh()
        return super.onPrepareOptionsMenu(menu)
    }

    protected open fun onViewsBound() {
        // ButterKnife was once right here

        toolbar?.let { setSupportActionBar(it) }
        supportActionBar?.setDisplayHomeAsUpEnabled(showUpNavigation)
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        onViewsBound()
    }

    protected open val showUpNavigation = false

    override fun toString() = activityName

}
