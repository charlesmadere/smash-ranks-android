package com.garpr.android.features.settings

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.garpr.android.R
import com.garpr.android.features.common.views.SimplePreferenceView
import java.text.NumberFormat

class DeleteFavoritePlayersPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), DialogInterface.OnClickListener, View.OnClickListener {

    var listener: Listener? = null

    interface Listener {
        fun onDeleteFavoritePlayersClick(v: DeleteFavoritePlayersPreferenceView)
    }

    companion object {
        private val NUMBER_FORMAT = NumberFormat.getIntegerInstance()
    }

    init {
        setLoading()
        setOnClickListener(this)
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        listener?.onDeleteFavoritePlayersClick(this)
    }

    override fun onClick(v: View) {
        AlertDialog.Builder(context)
                .setMessage(R.string.are_you_sure_you_want_to_delete_all_of_your_favorite_players)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes, this)
                .show()
    }

    fun setContent(size: Int) {
        isEnabled = size >= 1
        titleText = resources.getText(R.string.delete_all_favorite_players)
        descriptionText = resources.getQuantityString(R.plurals.x_favorites, size,
                NUMBER_FORMAT.format(size))
    }

    fun setLoading() {
        isEnabled = false
        titleText = resources.getText(R.string.loading_favorite_players_)
        descriptionText = resources.getText(R.string.please_wait_)
    }

}
