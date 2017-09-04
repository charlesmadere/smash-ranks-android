package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.view.View

import com.garpr.android.App
import com.garpr.android.BuildConfig
import com.garpr.android.R
import com.garpr.android.misc.NotificationsManager

import javax.inject.Inject

class TestNotificationView : SimplePreferenceView, DialogInterface.OnClickListener,
        View.OnClickListener {

    @Inject
    lateinit protected var mNotificationsManager: NotificationsManager


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onClick(dialog: DialogInterface, which: Int) {
        dialog.dismiss()

        when (which) {
            0 -> mNotificationsManager.cancelAll()
            1 -> mNotificationsManager.rankingsUpdated(context)
            else -> throw RuntimeException("illegal which: " + which)
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
