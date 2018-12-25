package com.garpr.android.views

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.garpr.android.R
import com.garpr.android.extensions.appComponent
import com.garpr.android.managers.FavoritePlayersManager
import java.text.NumberFormat
import javax.inject.Inject

class DeleteFavoritePlayersPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), DialogInterface.OnClickListener,
        FavoritePlayersManager.OnFavoritePlayersChangeListener, View.OnClickListener {

    private val numberFormat = NumberFormat.getIntegerInstance()

    @Inject
    protected lateinit var favoritePlayersManager: FavoritePlayersManager


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        favoritePlayersManager.addListener(this)
        refresh()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        favoritePlayersManager.clear()
    }

    override fun onClick(v: View) {
        AlertDialog.Builder(context)
                .setMessage(R.string.are_you_sure_you_want_to_delete_all_of_your_favorite_players)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes, this)
                .show()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        favoritePlayersManager.removeListener(this)
    }

    override fun onFavoritePlayersChange(favoritePlayersManager: FavoritePlayersManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
            favoritePlayersManager.addListener(this)
        }

        setOnClickListener(this)
        titleText = resources.getText(R.string.delete_all_favorite_players)

        if (isInEditMode) {
            descriptionText = resources.getQuantityString(R.plurals.x_favorites, 8,
                    numberFormat.format(8))
        } else {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        val size = favoritePlayersManager.size
        isEnabled = size != 0
        descriptionText = resources.getQuantityString(R.plurals.x_favorites, size,
                numberFormat.format(size))
    }

}
