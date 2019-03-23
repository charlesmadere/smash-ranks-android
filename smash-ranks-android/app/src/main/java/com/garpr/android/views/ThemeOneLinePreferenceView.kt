package com.garpr.android.views

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.activities.SplashActivity
import com.garpr.android.data.models.NightMode
import com.garpr.android.extensions.appComponent
import com.garpr.android.managers.NightModeManager
import javax.inject.Inject

class ThemeOneLinePreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : OneLinePreferenceView(context, attrs), DialogInterface.OnClickListener,
        NightModeManager.OnNightModeChangeListener, View.OnClickListener {

    @Inject
    protected lateinit var nightModeManager: NightModeManager


    init {
        titleText = context.getText(R.string.customize_theme)

        if (isInEditMode) {
            descriptionText = context.getText(R.string.system)
        }

        imageDrawable = ContextCompat.getDrawable(context, R.drawable.ic_chevron_down_white_18dp)

        setOnClickListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        nightModeManager.addListener(this)
        refresh()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        dialog.dismiss()

        val selected = NightMode.values()[which]
        if (nightModeManager.nightMode == selected) {
            return
        }
        nightModeManager.nightMode = selected

        Toast.makeText(context, R.string.app_restarted_to_apply_theme, Toast.LENGTH_SHORT).show()
        context.startActivity(SplashActivity.getLaunchIntent(context))
    }

    override fun onClick(v: View) {
        val items = nightModeManager.getNightModeStrings(context)
        val checkedItem = nightModeManager.nightMode.ordinal

        AlertDialog.Builder(context)
                .setSingleChoiceItems(items, checkedItem, this)
                .setTitle(R.string.theme)
                .show()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        nightModeManager.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
            nightModeManager.addListener(this)
            refresh()
        }
    }

    override fun onNightModeChange(nightModeManager: NightModeManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        descriptionText = context.getText(nightModeManager.nightMode.textResId)
    }

}
