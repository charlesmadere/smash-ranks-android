package com.garpr.android.activities

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.RegionManager.RegionHandle
import com.garpr.android.misc.Timber
import com.garpr.android.models.Region
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.views.toolbars.MenuToolbar
import kotterknife.bindOptionalView
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), Heartbeat, RegionHandle {

    @Inject
    lateinit protected var mGeneralPreferenceStore: GeneralPreferenceStore

    @Inject
    lateinit protected var mTimber: Timber

    protected val mToolbar: Toolbar? by bindOptionalView(R.id.toolbar)


    companion object {
        private val TAG = "BaseActivity"
        private val CNAME = BaseActivity::class.java.canonicalName
        internal val EXTRA_REGION = CNAME + ".Region"
    }

    protected abstract val activityName: String

    override fun getCurrentRegion(): Region? {
        if (intent != null && intent.hasExtra(EXTRA_REGION)) {
            return intent.getParcelableExtra<Region>(EXTRA_REGION)
        } else {
            return null
        }
    }

    protected var subtitle: CharSequence?
        get() {
            return supportActionBar?.subtitle
        }
        set(subtitle) {
            val actionBar = supportActionBar

            if (actionBar != null) {
                actionBar.subtitle = subtitle
            }
        }

    override fun isAlive(): Boolean {
        return !isFinishing && !isDestroyed
    }

    protected open fun navigateUp() {
        val upIntent = NavUtils.getParentActivityIntent(this)

        if (upIntent == null) {
            supportFinishAfterTransition()
        } else if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities()
        } else {
            supportNavigateUpTo(upIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.get().appComponent.inject(this)

        delegate.setLocalNightMode(mGeneralPreferenceStore.nightMode.get()!!.themeValue)

        super.onCreate(savedInstanceState)
        mTimber.d(TAG, activityName + " created")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (mToolbar is MenuToolbar) {
            (mToolbar as MenuToolbar).onCreateOptionsMenu(menuInflater, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navigateUp()
                return true
            }
        }

        if (mToolbar is MenuToolbar && (mToolbar as MenuToolbar).onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (mToolbar is MenuToolbar) {
            (mToolbar as MenuToolbar).refreshMenu()
        }

        return super.onPrepareOptionsMenu(menu)
    }

    protected open fun onViewsBound() {
        // ButterKnife was once right here

        if (mToolbar != null) {
            setSupportActionBar(mToolbar)
        }

        val actionBar = supportActionBar

        actionBar?.setDisplayHomeAsUpEnabled(showUpNavigation())
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        onViewsBound()
    }

    fun setSubtitle(@StringRes resId: Int) {
        subtitle = getText(resId)
    }

    protected open fun showUpNavigation(): Boolean {
        return false
    }

    override fun toString(): String {
        return activityName
    }

}