package com.garpr.android.views

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.view.View
import com.garpr.android.App
import com.garpr.android.BuildConfig
import com.garpr.android.R
import com.garpr.android.managers.NotificationsManager
import javax.inject.Inject

class TestNotificationView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), DialogInterface.OnClickListener, View.OnClickListener {

    @Inject
    protected lateinit var notificationsManager: NotificationsManager


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

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
        }

        setOnClickListener(this)
        titleText = resources.getText(R.string.show_test_notification)
        descriptionText = resources.getText(R.string.debug_only)
        visibility = if (BuildConfig.DEBUG) View.VISIBLE else View.GONE
    }

}
