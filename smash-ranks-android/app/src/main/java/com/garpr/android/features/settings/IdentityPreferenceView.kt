package com.garpr.android.features.settings

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.appComponent
import com.garpr.android.features.setIdentity.SetIdentityActivity
import com.garpr.android.managers.IdentityManager
import com.garpr.android.misc.RequestCodes
import com.garpr.android.views.SimplePreferenceView
import javax.inject.Inject

class IdentityPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), DialogInterface.OnClickListener,
        IdentityManager.OnIdentityChangeListener, View.OnClickListener {

    @Inject
    protected lateinit var identityManager: IdentityManager


    init {
        titleText = context.getText(R.string.identity)
        descriptionText = context.getText(R.string.easily_find_yourself_throughout_the_app)
        imageDrawable = ContextCompat.getDrawable(context, R.drawable.ic_face_white_24dp)
        setOnClickListener(this)

        if (!isInEditMode) {
            appComponent.inject(this)
        }
    }

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
            val activity = this.activity

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
