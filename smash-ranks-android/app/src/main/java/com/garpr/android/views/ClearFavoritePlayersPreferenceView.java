package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.FavoritePlayersManager;

import java.text.NumberFormat;

import javax.inject.Inject;

public class ClearFavoritePlayersPreferenceView extends SimplePreferenceView implements
        DialogInterface.OnClickListener, FavoritePlayersManager.OnFavoritePlayersChangeListener,
        View.OnClickListener {

    @Inject
    FavoritePlayersManager mFavoritePlayersManager;


    public ClearFavoritePlayersPreferenceView(@NonNull final Context context,
            @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public ClearFavoritePlayersPreferenceView(@NonNull final Context context,
            @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClearFavoritePlayersPreferenceView(@NonNull final Context context,
            @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr,
            @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) {
            return;
        }

        mFavoritePlayersManager.addListener(this);
        refresh();
    }

    @Override
    public void onClick(final DialogInterface dialog, final int which) {
        mFavoritePlayersManager.clear();
    }

    @Override
    public void onClick(final View v) {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.are_you_sure_you_want_to_clear_all_of_your_favorites)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes, this)
                .show();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFavoritePlayersManager.removeListener(this);
    }

    @Override
    public void onFavoritePlayersChanged(final FavoritePlayersManager manager) {
        if (ViewCompat.isAttachedToWindow(this)) {
            refresh();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOnClickListener(this);

        if (!isInEditMode()) {
            App.get().getAppComponent().inject(this);
            mFavoritePlayersManager.addListener(this);
        }

        setOnClickListener(this);
        setTitleText(R.string.clear_favorite_players);

        if (isInEditMode()) {
            return;
        }

        refresh();
    }

    @Override
    public void refresh() {
        super.refresh();

        final int size = mFavoritePlayersManager.size();
        setEnabled(size != 0);
        setDescriptionText(getResources().getQuantityString(R.plurals.x_favorites,
                size, NumberFormat.getInstance().format(size)));
    }

}
