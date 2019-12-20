package com.garpr.android.features.settings

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import com.garpr.android.R
import com.garpr.android.features.common.views.SimplePreferenceView

class RingtonePreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), View.OnClickListener {

    var listener: Listener? = null

    var ringtoneUri: Uri? = null
        set(value) {
            field = value

            val ringtone: Ringtone? = if (value == null) {
                null
            } else {
                RingtoneManager.getRingtone(context, value)
            }

            descriptionText = if (ringtone == null) {
                context.getText(R.string.none)
            } else {
                ringtone.getTitle(context)
            }
        }

    interface Listener {
        fun onClick(v: RingtonePreferenceView)
    }

    init {
        titleText = context.getText(R.string.ringtone)
        descriptionText = context.getText(R.string.none)
        setOnClickListener(this)
    }

    override fun onClick(v: View) {
        listener?.onClick(this)
    }

}
