package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.FavoritePlayersManager;

import java.text.NumberFormat;

import javax.inject.Inject;

public class ClearFavoritePlayersPreferenceView extends SimplePreferenceView implements
        FavoritePlayersManager.OnFavoritePlayersChangeListener, View.OnClickListener {

    private NumberFormat mNumberFormat;

    @Inject
    FavoritePlayersManager mFavoritePlayersManager;


    public ClearFavoritePlayersPreferenceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public ClearFavoritePlayersPreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClearFavoritePlayersPreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) {
            return;
        }

        mFavoritePlayersManager.addListener(this);
    }

    @Override
    public void onClick(final View v) {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFavoritePlayersManager.removeListener(this);
    }

    @Override
    public void onFavoritePlayersChanged(final FavoritePlayersManager manager) {
        refresh();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOnClickListener(this);
        mNumberFormat = java.text.NumberFormat.getInstance();

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
                size, mNumberFormat.format(size)));
    }

}
