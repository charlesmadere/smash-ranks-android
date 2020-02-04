package com.garpr.android.features.settings

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.data.models.NightMode
import com.garpr.android.features.common.views.SimplePreferenceView

class NightModePreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), DialogInterface.OnClickListener, View.OnClickListener {

    var listener: Listener? = null
    private var nightMode: NightMode? = null

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
        if (nightMode == selected) {
            return
        }

        Toast.makeText(context, R.string.restarting_app_to_apply_new_theme_, Toast.LENGTH_SHORT).show()
        listener?.onNightModeChange(this, selected)
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

    interface Listener {
        fun onNightModeChange(v: NightModePreferenceView, nightMode: NightMode)
    }

}
