package com.garpr.android.features.settings

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.data.models.NightMode
import com.garpr.android.extensions.appComponent
import com.garpr.android.features.common.views.SimplePreferenceView
import com.garpr.android.features.home.HomeActivity
import com.garpr.android.repositories.NightModeRepository
import javax.inject.Inject

class ThemePreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), DialogInterface.OnClickListener,
        NightModeRepository.OnNightModeChangeListener, View.OnClickListener {

    @Inject
    protected lateinit var nightModeRepository: NightModeRepository


    init {
        titleText = context.getText(R.string.theme)
        imageDrawable = ContextCompat.getDrawable(context, R.drawable.ic_format_paint_white_24dp)
        setOnClickListener(this)

        if (isInEditMode) {
            descriptionText = context.getText(R.string.auto)
        } else {
            appComponent.inject(this)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        nightModeRepository.addListener(this)
        refresh()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        dialog.dismiss()

        val selected = NightMode.values()[which]
        if (nightModeRepository.nightMode == selected) {
            return
        }

        AlertDialog.Builder(context)
                .setMessage(R.string.the_app_will_now_restart)
                .setNeutralButton(R.string.ok, null)
                .setOnDismissListener {
                    nightModeRepository.nightMode = selected
                    context.startActivity(HomeActivity.getLaunchIntent(context = context,
                            restartActivityTask = true))
                }
                .show()
    }

    override fun onClick(v: View) {
        val items = nightModeRepository.getNightModeStrings(context)
        val checkedItem = nightModeRepository.nightMode.ordinal

        AlertDialog.Builder(context)
                .setSingleChoiceItems(items, checkedItem, this)
                .setTitle(R.string.theme)
                .show()
    }

    override fun onDetachedFromWindow() {
        nightModeRepository.removeListener(this)
        super.onDetachedFromWindow()
    }

    override fun onNightModeChange(nightModeRepository: NightModeRepository) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        descriptionText = resources.getText(nightModeRepository.nightMode.textResId)
    }

}
