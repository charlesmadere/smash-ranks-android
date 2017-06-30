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
import com.garpr.android.R
import com.garpr.android.misc.FavoritePlayersManager
import java.text.NumberFormat
import javax.inject.Inject

class DeleteFavoritePlayersPreferenceView : SimplePreferenceView, DialogInterface.OnClickListener,
        FavoritePlayersManager.OnFavoritePlayersChangeListener, View.OnClickListener {

    @Inject
    lateinit protected var mFavoritePlayersManager: FavoritePlayersManager


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        mFavoritePlayersManager.addListener(this)
        refresh()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        mFavoritePlayersManager.clear()
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
        mFavoritePlayersManager.removeListener(this)
    }

    override fun onFavoritePlayersChanged(manager: FavoritePlayersManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setOnClickListener(this)

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
            mFavoritePlayersManager.addListener(this)
        }

        setOnClickListener(this)
        setTitleText(R.string.delete_favorite_players)

        if (isInEditMode) {
            return
        }

        refresh()
    }

    override fun refresh() {
        super.refresh()

        val size = mFavoritePlayersManager.size()
        isEnabled = size != 0
        setDescriptionText(resources.getQuantityString(R.plurals.x_favorites, size,
                NumberFormat.getInstance().format(size.toLong())))
    }

}
