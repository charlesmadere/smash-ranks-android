package com.garpr.android.features.settings

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View.OnClickListener
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.features.common.views.SimplePreferenceView

class IdentityPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), DialogInterface.OnClickListener {

    var listeners: Listeners? = null

    private val deleteIdentityClickListener = OnClickListener {
        AlertDialog.Builder(context)
                .setMessage(R.string.are_you_sure_you_want_to_delete_your_identity)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes, this)
                .show()
    }

    private val setIdentityClickListener = OnClickListener {
        listeners?.onSetIdentityClick()
    }

    interface Listeners {
        fun onDeleteIdentityClick()
        fun onSetIdentityClick()
    }

    init {
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_face_white_24dp))
        setLoading()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        listeners?.onDeleteIdentityClick()
    }

    fun setContent(identity: FavoritePlayer?) {
        isEnabled = true

        if (identity == null) {
            titleText = resources.getText(R.string.identity)
            descriptionText = resources.getText(R.string.easily_find_yourself_throughout_the_app)
            setOnClickListener(setIdentityClickListener)
        } else {
            titleText = resources.getText(R.string.delete_identity)
            descriptionText = identity.name
            setOnClickListener(deleteIdentityClickListener)
        }
    }

    fun setLoading() {
        isEnabled = false
        titleText = resources.getText(R.string.loading_identity_)
        descriptionText = resources.getText(R.string.please_wait_)
    }

}
