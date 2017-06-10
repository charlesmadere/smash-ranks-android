package com.garpr.android.views.toolbars;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.garpr.android.misc.Heartbeat;

public abstract class MenuToolbar extends Toolbar implements Heartbeat {

    private boolean mMenuCreated;
    private SparseBooleanArray mSparseMenuItemsArray;


    public MenuToolbar(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuToolbar(final Context context, @Nullable final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void createSparseMenuItemsArray() {
        mSparseMenuItemsArray = new SparseBooleanArray();

        final Menu menu = getMenu();

        for (int i = 0; i < menu.size(); ++i) {
            final MenuItem menuItem = menu.getItem(i);
            mSparseMenuItemsArray.put(menuItem.getItemId(), menuItem.isVisible());
        }
    }

    @Override
    public boolean isAlive() {
        return ViewCompat.isAttachedToWindow(this);
    }

    protected boolean isMenuCreated() {
        return mMenuCreated;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) {
            return;
        }

        refreshMenu();
    }

    public void onCreateOptionsMenu(final MenuInflater inflater, final Menu menu) {
        createSparseMenuItemsArray();
        mMenuCreated = true;
    }

    public boolean onOptionsItemSelected(final MenuItem item) {
        // intentionally empty, children can override
        return false;
    }

    public void onRefreshMenu() {
        final Menu menu = getMenu();

        for (int i = 0; i < menu.size(); ++i) {
            final MenuItem menuItem = menu.getItem(i);
            menuItem.setVisible(mSparseMenuItemsArray.get(menuItem.getItemId()));
        }
    }

    protected final void postRefreshMenu() {
        post(new Runnable() {
            @Override
            public void run() {
                if (isAlive()) {
                    refreshMenu();
                }
            }
        });
    }

    public final void refreshMenu() {
        if (isMenuCreated()) {
            onRefreshMenu();
        }
    }

}
