package com.garpr.android.views

import android.content.Context
import android.content.DialogInterface
import android.support.annotation.AttrRes
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.view.View
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.activities.SetIdentityActivity
import com.garpr.android.extensions.optActivity
import com.garpr.android.misc.IdentityManager
import com.garpr.android.misc.RequestCodes
import javax.inject.Inject

class IdentityPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0
) : SimplePreferenceView(context, attrs, defStyleAttr), DialogInterface.OnClickListener,
        IdentityManager.OnIdentityChangeListener, View.OnClickListener {

    @Inject
    protected lateinit var identityManager: IdentityManager


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        identityManager.addListener(this)
        refresh()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        identityManager.removeIdentity()
    }

    override fun onClick(v: View) {
        if (identityManager.hasIdentity) {
            AlertDialog.Builder(context)
                    .setMessage(R.string.are_you_sure_you_want_to_delete_your_identity)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.yes, this)
                    .show()
        } else {
            val activity = context.optActivity()

            if (activity == null) {
                context.startActivity(SetIdentityActivity.getLaunchIntent(context))
            } else {
                activity.startActivityForResult(SetIdentityActivity.getLaunchIntent(activity),
                        RequestCodes.CHANGE_IDENTITY.value)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        identityManager.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
        }

        setOnClickListener(this)
        titleText = resources.getText(R.string.identity)
        descriptionText = resources.getText(R.string.easily_find_yourself_throughout_the_app)

        if (isInEditMode) {
            return
        }

        identityManager.addListener(this)
        refresh()
    }

    override fun onIdentityChange(identityManager: IdentityManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        val player = identityManager.identity

        if (player == null) {
            titleText = resources.getText(R.string.identity)
            descriptionText = resources.getText(R.string.easily_find_yourself_throughout_the_app)
        } else {
            titleText = resources.getText(R.string.delete_identity)
            descriptionText = player.name
        }
    }

}
