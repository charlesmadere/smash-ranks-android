package com.garpr.android.views.toolbars;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.FavoritePlayersManager;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.models.FullPlayer;
import com.garpr.android.models.Match;
import com.garpr.android.models.MatchesBundle;

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

        final Activity activity = MiscUtils.optActivity(getContext());
        final FullPlayer fullPlayer;
        final MatchesBundle matchesBundle;
        final Match.Result result;

        if (activity instanceof DataProvider) {
            fullPlayer = ((DataProvider) activity).getFullPlayer();
            matchesBundle = ((DataProvider) activity).getMatchesBundle();
            result = ((DataProvider) activity).getResult();
        } else {
            fullPlayer = null;
            matchesBundle = null;
            result = null;
        }

        if (fullPlayer == null) {
            return;
        }

        final Menu menu = getMenu();

        if (matchesBundle != null && matchesBundle.hasMatches()) {
            menu.findItem(R.id.miFilter).setVisible(true);
            menu.findItem(R.id.miFilterAll).setVisible(result != null);
            menu.findItem(R.id.miFilterLosses).setVisible(result != Match.Result.LOSE);
            menu.findItem(R.id.miFilterWins).setVisible(result != Match.Result.WIN);
        }

        menu.findItem(R.id.miShare).setVisible(true);

        if (mFavoritePlayersManager.containsPlayer(fullPlayer)) {
            menu.findItem(R.id.miRemoveFromFavorites).setVisible(true);
        } else {
            menu.findItem(R.id.miAddToFavorites).setVisible(true);
        }

        if (fullPlayer.hasAliases()) {
            menu.findItem(R.id.miAliases).setVisible(true);
        }

        if (mIdentityManager.hasIdentity()) {
            final MenuItem menuItem = menu.findItem(R.id.miViewYourselfVsThisOpponent);
            menuItem.setTitle(getResources().getString(R.string.view_yourself_vs_x, fullPlayer.getName()));
            menuItem.setVisible(true);
        }
    }


    public interface DataProvider {
        @Nullable
        FullPlayer getFullPlayer();

        @Nullable
        MatchesBundle getMatchesBundle();

        @Nullable
        Match.Result getResult();
    }

}
