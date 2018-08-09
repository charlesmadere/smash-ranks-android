package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import android.util.SparseIntArray
import android.widget.LinearLayout
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.misc.Timber
import kotterknife.bindView

class TimberEntryItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs), BaseAdapterView<Timber.Entry> {

    private val colors = SparseIntArray()
    private val stackTrace: TextView by bindView(R.id.tvStackTrace)
    private val tagAndMessage: TextView by bindView(R.id.tvTagAndMessage)


    override fun setContent(content: Timber.Entry) {
        tagAndMessage.text = resources.getString(R.string.tag_and_message, content.tag,
                content.msg)

        if (content.stackTrace.isNullOrEmpty()) {
            stackTrace.clear()
            stackTrace.visibility = GONE
        } else {
            stackTrace.text = content.stackTrace
            stackTrace.visibility = VISIBLE
        }

        var color = colors.get(content.color, -1)

        if (color == -1) {
            color = context.getAttrColor(content.color)
            colors.put(content.color, color)
        }

        tagAndMessage.setTextColor(color)
        stackTrace.setTextColor(color)
    }

}
