package com.garpr.android.features.settings

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.garpr.android.BuildConfig
import com.garpr.android.R
import com.garpr.android.features.common.views.SimplePreferenceView
import com.garpr.android.features.notifications.NotificationsManager
import org.koin.core.KoinComponent
import org.koin.core.inject

class TestNotificationsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), DialogInterface.OnClickListener, KoinComponent,
        View.OnClickListener {

    protected val notificationsManager: NotificationsManager by inject()

    init {
        titleText = context.getText(R.string.show_test_notification)
        descriptionText = context.getText(R.string.debug_only)
        visibility = if (BuildConfig.DEBUG) View.VISIBLE else View.GONE
        setOnClickListener(this)
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        dialog.dismiss()

        when (which) {
            0 -> notificationsManager.cancelRankingsUpdated()
            1 -> notificationsManager.showRankingsUpdated()
            else -> throw RuntimeException("illegal which: $which")
        }
    }

    override fun onClick(v: View) {
        val items = arrayOf(resources.getText(R.string.cancel_rankings),
                resources.getText(R.string.show))

        AlertDialog.Builder(context)
                .setItems(items, this)
                .show()
    }

}
