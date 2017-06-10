package com.garpr.android.views.toolbars;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.FavoritePlayersManager;
import com.garpr.android.misc.IdentityManager;

import javax.inject.Inject;

public class PlayerToolbar extends SearchToolbar implements
        FavoritePlayersManager.OnFavoritePlayersChangeListener,
        IdentityManager.OnIdentityChangeListener {

    @Inject
    FavoritePlayersManager mFavoritePlayersManager;

    @Inject
    IdentityManager mIdentityManager;


    public PlayerToolbar(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerToolbar(final Context context, @Nullable final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) {
            return;
        }

        mFavoritePlayersManager.addListener(this);
        mIdentityManager.addListener(this);
    }

    @Override
    public void onCreateOptionsMenu(final MenuInflater inflater, final Menu menu) {
        inflater.inflate(R.menu.toolbar_player, menu);
        super.onCreateOptionsMenu(inflater, menu);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFavoritePlayersManager.removeListener(this);
        mIdentityManager.removeListener(this);
    }

    @Override
    public void onFavoritePlayersChanged(final FavoritePlayersManager manager) {
        if (isAlive()) {
            postRefreshMenu();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (!isInEditMode()) {
            App.get().getAppComponent().inject(this);
            mFavoritePlayersManager.addListener(this);
            mIdentityManager.addListener(this);
        }
    }

    @Override
    public void onIdentityChange(final IdentityManager identityManager) {
        if (isAlive()) {
            postRefreshMenu();
        }
    }

    @Override
    public void onRefreshMenu() {
        super.onRefreshMenu();


    }




    public interface Listeners extends Listener {
        @NonNull
        CharSequence getFullPlayerName();

        boolean showAliasesMenuItem();

        boolean showShareMenuItem();
    }

}
