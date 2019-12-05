package com.garpr.android.features.settings

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.extensions.activity
import com.garpr.android.features.common.views.SimplePreferenceView
import com.garpr.android.features.setIdentity.SetIdentityActivity
import com.garpr.android.misc.RequestCodes
import com.garpr.android.repositories.IdentityRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class IdentityPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), DialogInterface.OnClickListener,
        IdentityRepository.OnIdentityChangeListener, KoinComponent, View.OnClickListener {

    protected val identityRepository: IdentityRepository by inject()

    init {
        titleText = context.getText(R.string.identity)
        descriptionText = context.getText(R.string.easily_find_yourself_throughout_the_app)
        imageDrawable = ContextCompat.getDrawable(context, R.drawable.ic_face_white_24dp)
        setOnClickListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        identityRepository.addListener(this)
        refresh()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        identityRepository.removeIdentity()
    }

    override fun onClick(v: View) {
        if (identityRepository.hasIdentity) {
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
        identityRepository.removeListener(this)
        super.onDetachedFromWindow()
    }

    override fun onIdentityChange(identityRepository: IdentityRepository) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        val player = identityRepository.identity

        if (player == null) {
            titleText = resources.getText(R.string.identity)
            descriptionText = resources.getText(R.string.easily_find_yourself_throughout_the_app)
        } else {
            titleText = resources.getText(R.string.delete_identity)
            descriptionText = player.name
        }
    }

}
