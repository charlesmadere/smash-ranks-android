package com.garpr.android.features.settings

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.garpr.android.BuildConfig
import com.garpr.android.R
import com.garpr.android.extensions.appComponent
import com.garpr.android.features.common.views.SimplePreferenceView
import com.garpr.android.features.notifications.NotificationsManager
import javax.inject.Inject

class TestNotificationsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), DialogInterface.OnClickListener, View.OnClickListener {

    @Inject
    protected lateinit var notificationsManager: NotificationsManager


    init {
        titleText = context.getText(R.string.show_test_notification)
        descriptionText = context.getText(R.string.debug_only)
        visibility = if (BuildConfig.DEBUG) View.VISIBLE else View.GONE
        setOnClickListener(this)

        if (!isInEditMode) {
            appComponent.inject(this)
        }
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        dialog.dismiss()

        when (which) {
            0 -> notificationsManager.cancelAll()
            1 -> notificationsManager.rankingsUpdated()
            else -> throw RuntimeException("illegal which: $which")
        }
    }

    override fun onClick(v: View) {
        val items = arrayOf(resources.getText(R.string.cancel_all),
                resources.getText(R.string.show))

        AlertDialog.Builder(context)
                .setItems(items, this)
                .show()
    }

}
