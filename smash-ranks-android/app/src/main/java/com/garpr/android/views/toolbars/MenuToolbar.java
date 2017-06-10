package com.garpr.android.views.toolbars;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.garpr.android.misc.Heartbeat;

public abstract class MenuToolbar extends Toolbar implements Heartbeat {

    private boolean mMenuCreated;


    public MenuToolbar(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuToolbar(final Context context, @Nullable final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isAlive() {
        return ViewCompat.isAttachedToWindow(this);
    }

    protected boolean isMenuCreated() {
        return mMenuCreated;
    }

    public void onCreateOptionsMenu(final MenuInflater inflater, final Menu menu) {
        mMenuCreated = true;
    }

    public boolean onOptionsItemSelected(final MenuItem item) {
        // intentionally empty, children can override
        return false;
    }

    public abstract void onRefreshMenu();

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
