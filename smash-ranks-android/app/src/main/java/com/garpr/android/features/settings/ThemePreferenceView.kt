package com.garpr.android.features.settings

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.data.models.NightMode
import com.garpr.android.features.common.views.SimplePreferenceView

class ThemePreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), DialogInterface.OnClickListener, View.OnClickListener {

    var listener: Listener? = null
    private var nightMode: NightMode? = null

    interface Listener {
        fun onNightModeChange(v: ThemePreferenceView, nightMode: NightMode)
    }

    init {
        titleText = context.getText(R.string.theme)
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_format_paint_white_24dp))
        setOnClickListener(this)

        if (isInEditMode) {
            descriptionText = context.getText(R.string.auto)
        }
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        dialog.dismiss()

        val selected = NightMode.values()[which]
        if (checkNotNull(nightMode) == selected) {
            return
        }

        AlertDialog.Builder(context)
                .setMessage(R.string.the_app_will_now_restart)
                .setNeutralButton(R.string.ok, null)
                .setOnDismissListener {
                    listener?.onNightModeChange(this@ThemePreferenceView, selected)
                }
                .show()
    }

    override fun onClick(v: View) {
        val items = NightMode.values()
                .map { resources.getText(it.textResId) }
                .toTypedArray()

        val checkedItem = checkNotNull(nightMode).ordinal

        AlertDialog.Builder(context)
                .setSingleChoiceItems(items, checkedItem, this)
                .setTitle(R.string.theme)
                .show()
    }

    fun setContent(nightMode: NightMode) {
        this.nightMode = nightMode
        descriptionText = resources.getText(nightMode.textResId)
    }

}
