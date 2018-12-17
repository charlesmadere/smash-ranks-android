package com.garpr.android.views

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.garpr.android.R
import com.garpr.android.activities.HomeActivity
import com.garpr.android.extensions.appComponent
import com.garpr.android.misc.Timber
import com.garpr.android.models.NightMode
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.preferences.Preference
import javax.inject.Inject

class ThemePreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), DialogInterface.OnClickListener,
        Preference.OnPreferenceChangeListener<NightMode>, View.OnClickListener {

    @Inject
    protected lateinit var generalPreferenceStore: GeneralPreferenceStore

    @Inject
    protected lateinit var timber: Timber

    companion object {
        private const val TAG = "ThemePreferenceView"
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        generalPreferenceStore.nightMode.addListener(this)
        refresh()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        dialog.dismiss()

        val current = generalPreferenceStore.nightMode.get()
        val selected = NightMode.values()[which]

        if (current == selected) {
            return
        }

        AlertDialog.Builder(context)
                .setMessage(R.string.the_app_will_now_restart)
                .setNeutralButton(R.string.ok, null)
                .setOnDismissListener {
                    timber.d(TAG, "theme was \"$current\", is now \"$selected\"")
                    generalPreferenceStore.nightMode.set(selected)
                    refresh()

                    AppCompatDelegate.setDefaultNightMode(selected.themeValue)
                    context.startActivity(HomeActivity.getLaunchIntent(context = context,
                            restartActivityTask = true))
                }
                .show()
    }

    override fun onClick(v: View) {
        val items = arrayOfNulls<CharSequence>(NightMode.values().size)

        for (i in 0 until NightMode.values().size) {
            items[i] = resources.getText(NightMode.values()[i].textResId)
        }

        val current = generalPreferenceStore.nightMode.get()
        val checkedItem = current?.ordinal ?: -1

        AlertDialog.Builder(context)
                .setSingleChoiceItems(items, checkedItem, this)
                .setTitle(R.string.theme)
                .show()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        generalPreferenceStore.nightMode.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
        }

        setOnClickListener(this)
        titleText = resources.getText(R.string.theme)

        if (isInEditMode) {
            descriptionText = resources.getText(R.string.auto)
        } else {
            generalPreferenceStore.nightMode.addListener(this)
            refresh()
        }
    }

    override fun onPreferenceChange(preference: Preference<NightMode>) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        val nightMode = generalPreferenceStore.nightMode.get()
        descriptionText = resources.getText(nightMode?.textResId ?: R.string.not_yet_set)
    }

}
