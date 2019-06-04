package com.garpr.android.features.settings

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.garpr.android.R
import com.garpr.android.extensions.appComponent
import com.garpr.android.features.common.SimplePreferenceView
import com.garpr.android.repositories.FavoritePlayersRepository
import java.text.NumberFormat
import javax.inject.Inject

class DeleteFavoritePlayersPreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SimplePreferenceView(context, attrs), DialogInterface.OnClickListener,
        FavoritePlayersRepository.OnFavoritePlayersChangeListener, View.OnClickListener {

    private val numberFormat = NumberFormat.getIntegerInstance()

    @Inject
    protected lateinit var favoritePlayersRepository: FavoritePlayersRepository


    init {
        titleText = context.getText(R.string.delete_all_favorite_players)
        setOnClickListener(this)

        if (isInEditMode) {
            descriptionText = resources.getQuantityString(R.plurals.x_favorites, 8,
                    numberFormat.format(8))
        } else {
            appComponent.inject(this)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        favoritePlayersRepository.addListener(this)
        refresh()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        favoritePlayersRepository.clear()
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

        favoritePlayersRepository.removeListener(this)
    }

    override fun onFavoritePlayersChange(favoritePlayersRepository: FavoritePlayersRepository) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        val size = favoritePlayersRepository.size
        isEnabled = size != 0
        descriptionText = resources.getQuantityString(R.plurals.x_favorites, size,
                numberFormat.format(size))
    }

}
